package kafka.consumer;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

/**
 * @author dmytro.malovichko
 */
@ConfigurationProperties(prefix="consumer")
public class ConsumerProperties {

    private String topicName;

    private String zookeeperConnect;

    private String groupId;

    private String zookeeperSessionTimeoutMs;

    private String zookeeperSyncTimeMs;

    private String autoCommitIntervalMs;

    private int numberOfThreads;

    private String storagePath;

    public ConsumerConfig config() {
        return new ConsumerConfig(get());
    }

    private Properties get() {
        final Properties properties = new Properties();
        properties.put("zookeeper.connect", zookeeperConnect);
        properties.put("group.id", groupId);
        properties.put("zookeeper.session.timeout.ms", zookeeperSessionTimeoutMs);
        properties.put("zookeeper.sync.time.ms", zookeeperSyncTimeMs);
        properties.put("auto.commit.interval.ms", autoCommitIntervalMs);
        return properties;
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

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public void setNumberOfThreads(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public void setZookeeperConnect(String zookeeperConnect) {
        this.zookeeperConnect = zookeeperConnect;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setZookeeperSessionTimeoutMs(String zookeeperSessionTimeoutMs) {
        this.zookeeperSessionTimeoutMs = zookeeperSessionTimeoutMs;
    }

    public void setZookeeperSyncTimeMs(String zookeeperSyncTimeMs) {
        this.zookeeperSyncTimeMs = zookeeperSyncTimeMs;
    }

    public void setAutoCommitIntervalMs(String autoCommitIntervalMs) {
        this.autoCommitIntervalMs = autoCommitIntervalMs;
    }
}
