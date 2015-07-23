package kafka;

import kafka.consumer.ConsumerProperties;
import kafka.consumer.DefaultConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dmytro.malovichko
 */
public class KafkaConsumerApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerApplication.class);

    public static void main(final String[] args) {
        final ConsumerProperties properties = ConsumerProperties.read();
        final DefaultConsumer consumer = new DefaultConsumer(properties);
        consumer.start();

        addCleanupHook(consumer);

        LOGGER.info("Kafka consumer application has started");
    }

    private static void addCleanupHook(final DefaultConsumer consumer) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                consumer.stop();
            }
        });
    }

}
