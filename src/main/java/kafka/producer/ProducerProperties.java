package kafka.producer;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;
import java.util.UUID;

/**
 * @author dmytro.malovichko
 */
@ConfigurationProperties(prefix="producer")
public class ProducerProperties {

    private String bootstrapServers;

    private int requestRequiredAcks;

    private String topicName;

    public Properties get() {
        final Properties properties = new Properties();
        properties.put("bootstrap.servers", bootstrapServers);
        properties.put("client.id", UUID.randomUUID().toString());
        properties.put("request.required.acks", requestRequiredAcks);
        return properties;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public void setRequestRequiredAcks(int requestRequiredAcks) {
        this.requestRequiredAcks = requestRequiredAcks;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicName() {
        return topicName;
    }
}
