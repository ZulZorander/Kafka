package kafka.storage;

import kafka.producer.Producer;
import kafka.web.dto.Document;
import kafka.web.dto.Documents;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

/**
 * @author dmytro.malovichko
 */
public class KafkaStorage implements Storage {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaStorage.class);

    @Value("${kafka.storage.path}")
    private String storagePath;

    @Autowired
    private Producer producer;

    @Autowired
    private ExecutorService kafkaExecutorService;

    @Override
    public DeferredResult<Document> get(final String id) {
        final DeferredDocumentReader reader = new DeferredDocumentReader(storagePath, id);
        kafkaExecutorService.submit(reader);

        return reader.getDeferred();
    }

    @Override
    public DeferredResult<Documents> create(final List<String> documents) {
        final Documents result = new Documents();

        final DeferredResultCallback callback = new DeferredResultCallback(documents.size(), result, kafkaExecutorService);

        for (String document : documents) {
            final String id = UUID.randomUUID().toString();

            result.getDocs().add(new Document(id, StringEscapeUtils.escapeXml11(document)));

            LOGGER.info("Sending message with id {}", id);

            producer.send(id, document, callback);
        }

        return callback.getDeferred();
    }

}
