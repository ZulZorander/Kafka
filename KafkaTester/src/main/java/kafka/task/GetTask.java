package kafka.task;

import kafka.Document;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.concurrent.ConcurrentLinkedQueue;

import static javax.ws.rs.core.Response.Status.OK;

/**
* @author dmytro.malovichko
*/
public class GetTask extends AbstractTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetTask.class);

    private static final long DEFAULT_TIMEOUT_PER_GET = 1000L;

    private final ConcurrentLinkedQueue<Document> storage;

    public GetTask(final ConcurrentLinkedQueue<Document> storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        while(true) {
            get();
            sleep(DEFAULT_TIMEOUT_PER_GET);
        }
    }

    private void get() {
        ResteasyClient client = getClient();

        Document storageDoc = storage.poll();
        while(storageDoc != null) {
            final Response response = client.target("http://localhost:8080/document/" + storageDoc.getId())
                    .request(MediaType.APPLICATION_XML)
                    .get();

            validate(response, storageDoc);

            storageDoc = storage.poll();
        }

        client.close();
    }

    private void validate(final Response response, final Document storageDoc) {
        if (validateStatus(response)) {
            final Document responseDoc = parse(response);
            if (validateContent(storageDoc, responseDoc)) {
                LOGGER.info("GET. Successfully retrieved {}", storageDoc);
            } else {
                LOGGER.error("GET. Document content does not match. {} != {}", storageDoc.getContent(), responseDoc.getContent());
            }
        } else {
            LOGGER.error("GET. Invalid response status {} for document {}", response.getStatus(), storageDoc);
        }
    }

    private Document parse(final Response response) {
        final String entity = response.readEntity(String.class);

        return deserializeDoc(entity);
    }

    private boolean validateContent(final Document doc, final Document responseEntity) {
        return doc.getContent().equals(responseEntity.getContent());
    }

    private boolean validateStatus(final Response response) {
        return response.getStatus() == OK.getStatusCode();
    }

}
