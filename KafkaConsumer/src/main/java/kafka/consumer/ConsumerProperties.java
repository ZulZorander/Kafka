package kafka.consumer;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author dmytro.malovichko
 */
public class ConsumerProperties {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerProperties.class);

    private static final String CONSUMER_PREFIX = "consumer.";

    private final Properties consumerProperties;

    private final String topicName;

    private final int numberOfThreads;

    private final String storagePath;

    private ConsumerProperties(final Properties properties) {
        this.topicName = properties.getProperty("topic.name");
        this.numberOfThreads = Integer.valueOf(properties.getProperty("number.of.threads"));
        this.storagePath = properties.getProperty("storage.path");

        consumerProperties = new Properties();
        for (String property : properties.stringPropertyNames()) {
            if (property.startsWith(CONSUMER_PREFIX)) {
                final String consumerProperty = StringUtils.stripStart(property, CONSUMER_PREFIX);
                consumerProperties.setProperty(consumerProperty, properties.getProperty(property));
            }
        }
        LOGGER.info(consumerProperties.toString());
    }

    public static ConsumerProperties read() {
        return new ConsumerProperties(readProperties());
    }

    private static Properties readProperties() {
        Properties props = null;
        InputStream in = null;
        try {
            in = ConsumerProperties.class.getResourceAsStream("application.properties");
            props = new Properties();
            props.load(in);
            in.close();
        } catch (IOException e) {
            LOGGER.error("Failed to load application properties");
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(in);
        }

        return props;
    }

    public ConsumerConfig config() {
        return new ConsumerConfig(consumerProperties);
    }

    public String getTopicName() {
        return topicName;
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    public String getStoragePath() {
        return storagePath;
    }
}
