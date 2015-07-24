package kafka.consumer;


import kafka.javaapi.consumer.ConsumerConnector;
import kafka.serializer.StringDecoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author dmytro.malovichko
 */
public class DefaultConsumer {

    private final ConsumerProperties properties;

    private ConsumerConnector connector;

    private ExecutorService executor;

    public DefaultConsumer(final ConsumerProperties properties) {
        this.properties = properties;
    }

    public void init() {
        connector = Consumer.createJavaConsumerConnector(properties.config());

        final Map<String, List<KafkaStream<String, String>>> consumerMap = connector.createMessageStreams(populateMessageStreamsMap(),
                new StringDecoder(null), new StringDecoder(null));

        executor = Executors.newFixedThreadPool(properties.getNumberOfThreads());

        final OffsetCommitSemaphore offsetCommitSemaphore = new OffsetCommitSemaphore(connector, properties.getNumberOfThreads());
        startThreads(consumerMap, offsetCommitSemaphore);
        offsetCommitSemaphore.init();
    }

    public void destroy() {
        if (connector != null) connector.shutdown();
        if (executor != null) executor.shutdownNow();
    }

    private void startThreads(final Map<String, List<KafkaStream<String, String>>> consumerMap, final OffsetCommitSemaphore offsetCommitSemaphore) {
        int threadNumber = 0;
        for (final KafkaStream<String, String> stream : consumerMap.get(properties.getTopicName())) {
            executor.submit(new ConsumerThread(offsetCommitSemaphore, stream, threadNumber, properties.getStoragePath()));
            threadNumber++;
        }
    }

    private Map<String, Integer> populateMessageStreamsMap() {
        final Map<String, Integer> topicCountMap = new HashMap<>();
        topicCountMap.put(properties.getTopicName(), properties.getNumberOfThreads());
        return topicCountMap;
    }

}
