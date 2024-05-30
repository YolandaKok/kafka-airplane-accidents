package com.yk.project.kafka.airplane.accidents.producer.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${app.kafka.topics.raw}")
    private String rawTopic;

    @Bean
    public KafkaAdmin generateKafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configs.put(AdminClientConfig.CLIENT_ID_CONFIG, "local-admin-1");
        return new KafkaAdmin(configs);
    }

    @Bean
    public KafkaTemplate<Long, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    private ProducerFactory<Long, Object> producerFactory() {
        var configProperties = getDefaultConfigurationProperties();
        return new DefaultKafkaProducerFactory<>(configProperties);
    }

    private Map<String, Object> getDefaultConfigurationProperties() {
        Map<String, Object> configProperties = new HashMap<>();
        configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        configProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProperties.put(ProducerConfig.BATCH_SIZE_CONFIG, "16384");
        configProperties.put(ProducerConfig.LINGER_MS_CONFIG, "0");
        configProperties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, "30000");
        configProperties.put(ProducerConfig.ACKS_CONFIG, "all");
        configProperties.put(ProducerConfig.CLIENT_ID_CONFIG, "accidents-producer");
        configProperties.put(ProducerConfig.RETRIES_CONFIG, "3");
        configProperties.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, "true");

        return configProperties;
    }

    @Bean
    public NewTopic rawTopic() {
        return TopicBuilder
                .name(rawTopic)
                .partitions(3)
                .replicas(2)
                .config(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, "2")
                .build();
    }

    @Bean
    public NewTopic cleanUpTopic() {
        return TopicBuilder
                .name("clean-data-topic")
                .partitions(3)
                .replicas(2)
                .build();
    }

    @Bean
    public NewTopic countTopic() {
        return TopicBuilder
                .name("count-per-year-month-species")
                .partitions(3)
                .replicas(2)
                .compact()
                .build();
    }

    @Bean
    public NewTopic windowTopic() {
        Map<String, String> configs =
                Map.of(TopicConfig.RETENTION_MS_CONFIG, "10000",
                        TopicConfig.DELETE_RETENTION_MS_CONFIG, "100",
                        TopicConfig.SEGMENT_MS_CONFIG, "100");
        return TopicBuilder
                .name("sliding-window-result")
                .partitions(3)
                .replicas(2)
                .compact()
                .configs(configs)
                .build();
    }
}
