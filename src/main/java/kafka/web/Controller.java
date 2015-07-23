package kafka.web;

import kafka.storage.Storage;
import kafka.web.dto.Document;
import kafka.web.dto.Documents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

/**
 * @author dmytro.malovichko
 */
@RestController
final public class Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    @Autowired
    private Storage kafkaStorage;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/document/{id}", method = RequestMethod.GET, produces = "application/xml")
    DeferredResult<Document> read(@PathVariable final String id) {
        LOGGER.info("Received read request for id {}", id);
        return kafkaStorage.get(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/document", method = RequestMethod.POST, consumes = "application/xml", produces = "application/xml")
    DeferredResult<Documents> write(@RequestBody final List<String> documents) {
        for (String document : documents) {
            LOGGER.info("Received write request for document {}", document);
        }

        return kafkaStorage.create(documents);
    }

}
