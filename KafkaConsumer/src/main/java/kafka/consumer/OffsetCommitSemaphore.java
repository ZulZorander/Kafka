package kafka.consumer;

import kafka.javaapi.consumer.ConsumerConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author dmytro.malovichko
 */
public class OffsetCommitSemaphore {

    private static final Logger LOGGER = LoggerFactory.getLogger(OffsetCommitSemaphore.class);

    private final ConsumerConnector connector;

    private final Semaphore semaphore;

    private final int numberOfThreads;

    private final Timer timer = new Timer();

    private AtomicBoolean started = new AtomicBoolean(false);

    public OffsetCommitSemaphore(final ConsumerConnector connector, final int numberOfThreads) {
        this.connector = connector;
        this.semaphore = new Semaphore(numberOfThreads);
        this.numberOfThreads = numberOfThreads;
    }

    public void lazyInit() {
        if (!started.getAndSet(true)) {
            init();
        }
    }

    private void init() {
        final TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                LOGGER.info("Waiting for semaphore to block");
                semaphore.acquireUninterruptibly(numberOfThreads);
                connector.commitOffsets(true);
                LOGGER.info("Offsets are committed. Releasing semaphore");
                semaphore.release(numberOfThreads);
            }
        };

        timer.schedule(timerTask, 5000L, 5000L);
    }

    public void acquire() throws InterruptedException {
        semaphore.acquire();
    }

    public void release() {
        semaphore.release();
    }

}
