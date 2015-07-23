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

    public void start() {
        connector = Consumer.createJavaConsumerConnector(properties.config());

        Map<String, List<KafkaStream<String, String>>> consumerMap = connector.createMessageStreams(populateMessageStreamsMap(),
                new StringDecoder(null), new StringDecoder(null));

        executor = Executors.newFixedThreadPool(properties.getNumberOfThreads());

        startThreads(consumerMap);
    }

    public void stop() {
        if (connector != null) connector.shutdown();
        if (executor != null) executor.shutdown();
    }

    private void startThreads(final Map<String, List<KafkaStream<String, String>>> consumerMap) {
        int threadNumber = 0;
        for (final KafkaStream<String, String> stream : consumerMap.get(properties.getTopicName())) {
            executor.submit(new ConsumerThread(stream, threadNumber, properties.getStoragePath()));
            threadNumber++;
        }
    }

    private Map<String, Integer> populateMessageStreamsMap() {
        Map<String, Integer> topicCountMap = new HashMap<>();
        topicCountMap.put(properties.getTopicName(), properties.getNumberOfThreads());
        return topicCountMap;
    }

}
