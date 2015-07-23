package kafka.storage;

import kafka.web.dto.Document;
import kafka.web.dto.Documents;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

/**
 * @author dmytro.malovichko
 */
public interface Storage {

    DeferredResult<Document> get(String id);

    DeferredResult<Documents> create(List<String> documents);
}
