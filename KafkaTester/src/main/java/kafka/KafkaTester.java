package kafka;

import kafka.task.GetTask;
import kafka.task.PostTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author dmytro.malovichko
 */
public class KafkaTester {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaTester.class);

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    private final ConcurrentLinkedQueue<Document> storage = new ConcurrentLinkedQueue<>();

    public static void main(final String[] args) {
        new KafkaTester().start();
    }

    private void start() {
        LOGGER.info("Start testing");

        executorService.submit(new PostTask(storage));

        executorService.submit(new GetTask(storage));

        addCleanupHook();
    }

    private void addCleanupHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                executorService.shutdown();
            }
        });
    }

}
