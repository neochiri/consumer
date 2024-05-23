

import java.io.IOException;
import java.util.Properties;

public class Application {

    public static void main(String[] args) throws IOException {
        // create consumer configs
        final Properties properties = new Properties();
        properties.load(Application.class.getClassLoader().getResourceAsStream("application.properties"));


        final ConsumerService consumerService = new ConsumerService(properties);
        consumerService.start();
    }
}
