package com.example.config;

import com.example.model.VehicleSignal;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

@Configuration
public class VehicleSignalsKafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<Long, VehicleSignal> vehicleSignalsProducerFactory() {
        if (bootstrapServers == null) throw new RuntimeException("spring.kafka.bootstrap-servers property wasn't set");
        return new DefaultKafkaProducerFactory<>(
                Map.of(
                        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                        ProducerConfig.RETRIES_CONFIG, 3,
                        ProducerConfig.ACKS_CONFIG, "all",
                        ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true,
                        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class,
                        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
                ));
    }

    @Bean
    public KafkaTemplate<Long, VehicleSignal> vehicleSignalKafkaSender() {
        return new KafkaTemplate<>(vehicleSignalsProducerFactory());
    }

}
