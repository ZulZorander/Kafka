package kafka.storage;

import kafka.web.ReadFileException;
import kafka.web.ResourceNotFoundException;
import kafka.web.dto.Document;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.async.DeferredResult;

import java.io.File;

/**
* @author dmytro.malovichko
*/
public class DeferredDocumentReader implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeferredDocumentReader.class);

    private static final String EXT = ".xml";

    private final String storagePath;

    private final String id;

    private final DeferredResult<Document> deferred;

    public DeferredDocumentReader(final String storagePath, final String id) {
        this.deferred = new DeferredResult<>();
        this.storagePath = storagePath;
        this.id = id;
    }

    @Override
    public void run() {
        final File file = new File(storagePath, id + EXT);

        if (!file.exists()) {
            LOGGER.debug("Document with id {} does not exist", id);
            deferred.setErrorResult(new ResourceNotFoundException(id));
            return;
        }

        try {
            LOGGER.debug("Reading document with id {}", id);
            final String content = FileUtils.readFileToString(file);

            deferred.setResult(new Document(id, StringEscapeUtils.escapeXml11(content)));
        } catch (Exception e) {
            LOGGER.error(String.format("Cannot read document with id %s", id), e);
            deferred.setErrorResult(new ReadFileException(id));
        }
    }

    public DeferredResult<Document> getDeferred() {
        return deferred;
    }
}
