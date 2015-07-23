package kafka;

import kafka.consumer.ConsumerProperties;
import kafka.consumer.DefaultConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author dmytro.malovichko
 */
@Configuration
@ComponentScan
@EnableConfigurationProperties({ConsumerProperties.class})
public class KafkaConsumerApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerApplication.class);

    public static void main(final String[] args) {
        new SpringApplicationBuilder()
                .web(false) // not using Spring Boot web capabilities
                .sources(KafkaConsumerApplication.class)
                .run(args);

        LOGGER.info("Kafka consumer application has started");
    }

    @Bean(initMethod = "init", destroyMethod = "destroy")
    public DefaultConsumer consumer(final ConsumerProperties properties) {
        return new DefaultConsumer(properties);
    }

}
