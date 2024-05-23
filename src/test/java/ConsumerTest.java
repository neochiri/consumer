
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for testing a Kafka consumer and broker
 *
 *
 */
public class ConsumerTest {

    private static Properties properties = new Properties();

    @BeforeAll
    public static void setUp() throws IOException {
        properties.load(Application.class.getClassLoader().getResourceAsStream("application.properties"));
    }

    @Test
    public void connect_broker_success() {
        try (final Consumer<String, String> consumer = new KafkaConsumer<>(properties)) {
            /* If the connection has been lost, we will get an Exception
             * Default timeout is 30 seconds, so we reduce it to 1 second
             */
            assertDoesNotThrow(() -> consumer.listTopics(Duration.ofMillis(1000)), "The Kafka broker is connected");
        }
    }

    @Test
    public void connect_broker_not_possible() {
        try (final Consumer<String, String> consumer = new KafkaConsumer<>(properties)) {
            // Forcing the closure of the consumer to assert if the behaviour is correct
            consumer.close();
            /* If the connection has been lost, we will get an Exception
             * Default timeout is 30 seconds, so we reduce it to 1 second
             */
            assertThrows(Exception.class, () -> consumer.listTopics(Duration.ofMillis(1000)), "The Kafka broker is disconnected");
        }
    }

    @Test
    public void subscribe_topic_success() {
        try (final Consumer<String, String> consumer = new KafkaConsumer<>(properties)) {
            // Subscribing to a topic
            consumer.subscribe(List.of("consumer_test"));
            // Listing topics to identify if the subscribed topic is in the list
            final Map<String, List<PartitionInfo>> topics = consumer.listTopics(Duration.ofMillis(1000));
            assertTrue(topics.containsKey("consumer_test"), "The topic has been subscribed");
        }
    }

    @Test
    public void consume_message_success() {
        try (final Consumer<String, String> consumer = new KafkaConsumer<>(properties)) {
            // Subscribing to the
            consumer.subscribe(List.of("consumer_test"));
            final ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(5000));
            assertTrue(records.count() > 0, "There are records in the consumer"); // WHY FAILS WITH A PRODUCER RUNNING??
        }
    }

    @Test
    public void keep_connection_alive_and_shutdown_success() throws InterruptedException {
        try (final Consumer<String, String> consumer = new KafkaConsumer<>(properties)) {
            // Keeping the connection alive for 1 minute
            Thread.sleep(60000);
            /* If the connection has been lost, we will get an Exception
             * Default timeout is 30 seconds, so we reduce it to 1 second
             */
            assertDoesNotThrow(() -> consumer.listTopics(Duration.ofMillis(1000)), "The Kafka broker is connected");
            assertDoesNotThrow(() -> consumer.close());
        }
    }
}
