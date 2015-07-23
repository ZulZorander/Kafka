package kafka;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

/**
 * @author dmytro.malovichko
 */
@JacksonXmlRootElement(localName = "documents")
public class Documents {

    public Documents() {
    }

    @JacksonXmlProperty(localName = "document")
    public List<Document> docs;

    public List<Document> getDocs() {
        return docs;
    }

    public void setDocs(List<Document> docs) {
        this.docs = docs;
    }

    @Override
    public String toString() {
        return "Documents{" +
                "docs=" + docs +
                '}';
    }
}

