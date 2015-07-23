package kafka.web.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dmytro.malovichko
 */
@JacksonXmlRootElement(localName = "documents")
public class Documents {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "document")
    private List<Document> docs = new ArrayList<>();

    public Documents() {
    }

    public List<Document> getDocs() {
        return docs;
    }

    public void setDocs(final List<Document> docs) {
        this.docs = docs;
    }
}

