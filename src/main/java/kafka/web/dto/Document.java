package kafka.web.dto;

/**
 * @author dmytro.malovichko
 */
public class Document {

    private String id;

    private String content;

    public Document() {
    }

    public Document(final String id, final String content) {
        this.id = id;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public void setContent(final String content) {
        this.content = content;
    }
}
