package kafka.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author dmytro.malovichko
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource is not found")
final public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(final String id) {
        super(String.format("Document with id %s is not found", id));
    }

}
