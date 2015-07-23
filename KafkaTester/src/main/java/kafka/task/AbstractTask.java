package kafka.task;

import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import kafka.Document;
import kafka.Documents;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author dmytro.malovichko
 */
public abstract class AbstractTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTask.class);

    void sleep(final long timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            LOGGER.error("Interrupted", e);
        }
    }

    ResteasyClient getClient() {
        return new ResteasyClientBuilder()
                .establishConnectionTimeout(10, TimeUnit.SECONDS)
                .socketTimeout(10, TimeUnit.SECONDS)
                .disableTrustManager()
                .build();
    }

    Document deserializeDoc(final String entity) {
        Document doc = null;
        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        XmlMapper xmlMapper = new XmlMapper(module);
        try {
            doc = xmlMapper.readValue(entity, Document.class);
        } catch (IOException e) {
            LOGGER.error("Failed to deserialize response entity " + entity, e);
        }

        return doc;
    }

    Documents deserializeDocs(final String entity) {
        Documents docs = null;
        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        XmlMapper xmlMapper = new XmlMapper(module);
        try {
            docs = xmlMapper.readValue(entity, Documents.class);
        } catch (IOException e) {
            LOGGER.error("Failed to deserialize response entity " + entity, e);
        }

        return docs;
    }
}
