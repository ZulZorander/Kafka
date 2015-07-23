package kafka.producer;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 * @author dmytro.malovichko
 */
public class DefaultProducer implements Producer {

    private KafkaProducer<String, String> producer;

    private final ProducerProperties properties;

    public DefaultProducer(final ProducerProperties properties) {
        this.properties = properties;
    }

    public void init() {
        producer = new KafkaProducer<>(properties.get(), new StringSerializer(), new StringSerializer());
    }

    public void destroy() {
        if (producer != null) {
            producer.close();
        }
    }

    @Override
    public void send(final String key, final String value, final Callback callback) {
        ProducerRecord<String, String> record = new ProducerRecord<>(properties.getTopicName(), key, value);
        producer.send(record, callback);
    }

}
