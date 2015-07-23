package kafka.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author dmytro.malovichko
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Failed read from file")
public class ReadFileException extends RuntimeException {

    public ReadFileException(final String id) {
        super(String.format("Failed to read document with id %s", id));
    }

}
