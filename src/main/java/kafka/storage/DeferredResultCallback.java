package kafka.storage;

import kafka.web.SendMessageException;
import kafka.web.dto.Documents;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author dmytro.malovichko
 */
public class DeferredResultCallback implements Callback {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeferredResultCallback.class);

    private final DeferredResult<Documents> deferred;

    private final ExecutorService executorService;

    private final AtomicInteger latch = new AtomicInteger();

    private final Documents result;

    public DeferredResultCallback(final int size, final Documents result, final ExecutorService executorService) {
        this.deferred = new DeferredResult<>();
        this.result = result;
        this.executorService = executorService;
        latch.set(size);
    }

    @Override
    public void onCompletion(final RecordMetadata metadata, final Exception e) {
        if (!deferred.isSetOrExpired()) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    if (e != null) {
                        LOGGER.error("Failed to send message to Kafka", e);
                        deferred.setErrorResult(new SendMessageException(e));
                    } else {
                        if (latch.decrementAndGet() == 0) {
                            deferred.setResult(result);
                        }
                        LOGGER.info("The offset of the record we just sent is: {}", metadata.offset());
                    }
                }
            });
        }
    }

    public DeferredResult<Documents> getDeferred() {
        return deferred;
    }
}
