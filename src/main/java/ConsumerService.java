import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

/**
 * Service class to run the logic of a Kafka consumer
 */
public class ConsumerService {

    private Consumer<String, String> consumer;

    public ConsumerService(final Properties properties) {
        this.consumer = new KafkaConsumer<>(properties);
    }

    /**
     * Subscribes to the list of methods given.
     *
     * @param topics Topics to subscribe to.
     */
    public void subscribe(final List<String> topics) {
        this.consumer.subscribe(topics);
    }

    /**
     * Subscribes to a list of methods
     * Polls the messages from the consumer
     * Prints the messages or an exception if needed
     *
     */
    public void start() {
        this.subscribe(List.of("consumer_test"));
        try {
            // It will only run as long as there is a connection to the consumer
            while (!this.consumer.listTopics(Duration.ofMillis(1000)).isEmpty()) {
                final ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (final ConsumerRecord<String, String> record : records) {
                    System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
                }
            }
        } catch (final Exception exception) {
            System.out.println("Error while consuming messages: " + exception.getMessage());
        } finally {
            this.shutdown();
        }
    }

    /**
     * Shuts down the consumer
     */
    public void shutdown() {
        this.consumer.close();
    }
}
