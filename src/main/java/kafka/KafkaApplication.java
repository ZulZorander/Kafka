package kafka;

import kafka.producer.DefaultProducer;
import kafka.producer.Producer;
import kafka.producer.ProducerProperties;
import kafka.storage.KafkaStorage;
import kafka.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author dmytro.malovichko
 */
@SpringBootApplication
@EnableConfigurationProperties({ProducerProperties.class})
public class KafkaApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(KafkaApplication.class, args);

        LOGGER.info("Kafka application has started");
    }

    @Bean(initMethod = "init", destroyMethod = "destroy")
    public Producer producer(final ProducerProperties properties) {
        return new DefaultProducer(properties);
    }

    @Bean
    public Storage kafkaStorage() {
        return new KafkaStorage();
    }

    @Bean(destroyMethod = "shutdownNow")
    public ExecutorService kafkaExecutorService(@Value("${message.batch.executor.pool.size}") final int poolSize) {
        return Executors.newFixedThreadPool(poolSize);
    }

}
