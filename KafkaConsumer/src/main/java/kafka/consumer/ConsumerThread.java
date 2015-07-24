package kafka.consumer;

import kafka.message.MessageAndMetadata;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author dmytro.malovichko
 */
public class ConsumerThread implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerThread.class);

    private static final String EXT = ".xml";

    private final KafkaStream<String, String> stream;

    private final String storagePath;

    private final int threadNumber;

    private final OffsetCommitSemaphore offsetCommitSemaphore;

    public ConsumerThread(final OffsetCommitSemaphore offsetCommitSemaphore, final KafkaStream<String, String> stream,
                          final int threadNumber, final String storagePath) {
        this.offsetCommitSemaphore = offsetCommitSemaphore;
        this.threadNumber = threadNumber;
        this.stream = stream;
        this.storagePath = storagePath;
    }

    @Override
    public void run() {
        for (MessageAndMetadata<String, String> data : stream) {
            LOGGER.info("Received message in thread {}, key {}, message {}", threadNumber, data.key(), data.message());
            processMessage(data);
        }
        LOGGER.info("Shutting down Thread: " + threadNumber);
    }

    private void processMessage(MessageAndMetadata<String, String> data) {
        try {
            offsetCommitSemaphore.lazyInit(); // not too fancy way of initializing semaphore
            offsetCommitSemaphore.acquire();
            // maybe it does makes sense to do IO in separate thread, testing is needed
            save(data.key(), data.message());
        } catch (InterruptedException e) {
            LOGGER.error(String.format("Failed to acquire semaphore in thread %s, cannot save key %s, message %s ", threadNumber, data.key(), data.message()), e);
        } finally {
            offsetCommitSemaphore.release();
        }
    }

    private void save(final String id, final String document) {
        try {
            FileUtils.writeStringToFile(new File(storagePath, id + EXT), document);
        } catch (Exception e) {
            LOGGER.error(String.format("Cannot write to file document with id %s", id), e);
        }
    }

}
