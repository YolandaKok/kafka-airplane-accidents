package com.yk.project.kafka.airplane.accidents.consumer.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.IsolationLevel;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {
	private final KafkaTemplate<Long, Object> kafkaTemplate;

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Value("${spring.kafka.consumer.group-id}")
	private String consumerGroupId;

	@Bean("multiTypeKafkaListenerContainerFactory")
	public ConcurrentKafkaListenerContainerFactory<String, Object> multiTypeKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Object> factory =
				new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(multiTypeConsumerFactory());
		factory.setConcurrency(1);
		factory.getContainerProperties().setPollTimeout(10000);
		factory.getContainerProperties().setIdleBetweenPolls(5000);
		return factory;
	}

	private ConsumerFactory<String, Object> multiTypeConsumerFactory() {
		var configProperties = getDefaultConfigurationProperties();
		return new DefaultKafkaConsumerFactory<>(configProperties);
	}

	private Map<String, Object> getDefaultConfigurationProperties() {
		Map<String, Object> configProperties = new HashMap<>();
		configProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		configProperties.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
		configProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configProperties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		configProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		configProperties.put(ConsumerConfig.DEFAULT_ISOLATION_LEVEL,
							 IsolationLevel.READ_COMMITTED.toString().toLowerCase());
		configProperties.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG,
							 "org.apache.kafka.clients.consumer.RoundRobinAssignor");
		configProperties.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, true);

		return configProperties;
	}
}
