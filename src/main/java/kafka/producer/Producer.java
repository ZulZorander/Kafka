package kafka.producer;

import org.apache.kafka.clients.producer.Callback;

/**
 * @author dmytro.malovichko
 */
public interface Producer {

    void send(String key, String value, Callback callback);

}
