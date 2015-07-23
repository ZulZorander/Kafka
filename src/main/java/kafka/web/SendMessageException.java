package kafka.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author dmytro.malovichko
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "One or more documents were not saved successfully")
final public class SendMessageException extends RuntimeException {

    public SendMessageException(final Exception e) {
        super("One or more documents were not saved successfully", e);
    }
}
