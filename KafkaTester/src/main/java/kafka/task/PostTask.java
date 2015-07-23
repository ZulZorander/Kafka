package kafka.task;

import kafka.Document;
import kafka.Documents;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.ConcurrentLinkedQueue;

import static javax.ws.rs.core.Response.Status.CREATED;

/**
* @author dmytro.malovichko
*/
public class PostTask extends AbstractTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostTask.class);

    private static final int DEFAULT_AMOUNT_OF_GENERATED_DOCS_PER_POST = 5;

    private static final long DEFAULT_TIMEOUT_PER_POST = 1000L;

    private final ConcurrentLinkedQueue<Document> storage;

    public PostTask(final ConcurrentLinkedQueue<Document> storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        while(true) {
            post();
            sleep(DEFAULT_TIMEOUT_PER_POST);
        }
    }

    private void post() {
        ResteasyClient client = getClient();

        final String documents = generateContent();

        LOGGER.info("Sending documents {}", documents);
        final Response response = client.target("http://localhost:8080/document")
                .request(MediaType.APPLICATION_XML)
                .post(Entity.entity(documents, MediaType.APPLICATION_XML));

        if (validateStatus(response)) {
            parseAndSave(response);
        } else {
            LOGGER.error("POST. Invalid response status {} for request {}", response.getStatus(), documents);
        }

        client.close();
    }

    private boolean validateStatus(final Response response) {
        return response.getStatus() == CREATED.getStatusCode();
    }

    private void parseAndSave(final Response response) {
        final String entity = response.readEntity(String.class);

        final Documents docs = deserializeDocs(entity);
        LOGGER.info("POST. Documents successfully sent {}", docs);

        storage.addAll(docs.getDocs());
    }

    private String generateContent() {
        String documents = "";
        for ( int i = 0 ; i <= RandomUtils.nextInt(0, DEFAULT_AMOUNT_OF_GENERATED_DOCS_PER_POST); i ++ ) {
            String document = String.format("<data>document%s</data>", RandomStringUtils.randomAlphabetic(20));
            final String escapedDoc = StringEscapeUtils.escapeXml11(document);
            documents += String.format("<document>%s</document>", escapedDoc);
        }
        documents = String.format("<documents>%s</documents>", documents);
        return documents;
    }

}
