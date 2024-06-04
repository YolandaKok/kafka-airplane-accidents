package com.yk.project.kafka.airplane.accidents.streams.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.apache.kafka.streams.KafkaStreams;
import org.springframework.kafka.config.TopicBuilder;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafkaStreams
public class KafkaStreamConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String streamConsumerGroupId;

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kafkaStreamsConfig() {
        var props = new HashMap<String, Object>();

        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "demo-kafka-stream");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, "2000");
        props.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE_V2);
        //		props.put(StreamsConfig.ROCKSDB_CONFIG_SETTER_CLASS_CONFIG, CustomRocksDBConfig.class);
        props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, 3);
        props.put(StreamsConfig.NUM_STANDBY_REPLICAS_CONFIG, 1);
        props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 2);
        props.put(StreamsConfig.producerPrefix(ProducerConfig.ACKS_CONFIG), "all");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, streamConsumerGroupId);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "8192");

        // For producer and consumer settings used internally
        // @see https://kafka.apache.org/10/documentation/streams/developer-guide/config-streams.html#kafka-consumers-and-producer-configuration-parameters

        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    public NewTopic cleanUpTopic() {
        return TopicBuilder
                .name("clean-data-topic")
                .partitions(3)
                .replicas(2)
                .config(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, "2")
                .build();
    }

    @Bean
    public NewTopic windowTopic() {
        Map<String, String> configs =
                Map.of(TopicConfig.RETENTION_MS_CONFIG, "1000",
                        TopicConfig.DELETE_RETENTION_MS_CONFIG, "100",
                        TopicConfig.SEGMENT_MS_CONFIG, "100");
        return TopicBuilder
                .name("sliding-window-result")
                .partitions(3)
                .replicas(2)
                .compact()
                .config(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, "2")
                .configs(configs)
                .build();
    }
}
