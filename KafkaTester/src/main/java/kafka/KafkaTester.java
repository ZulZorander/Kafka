package kafka;

import kafka.task.PostTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author dmytro.malovichko
 */
public class KafkaTester {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaTester.class);

    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    private final ExecutorService postExecutorService = Executors.newFixedThreadPool(50);

    public static void main(final String[] args) {
        new KafkaTester().start();
    }

    private void start() {
        LOGGER.info("Start testing");

        executorService.submit(new PostTask(postExecutorService));

        addCleanupHook();
    }

    private void addCleanupHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                executorService.shutdownNow();
                postExecutorService.shutdownNow();
            }
        });
    }

}
