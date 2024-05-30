package com.yk.project.kafka.airplane.accidents.consumer.consume;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yk.project.kafka.airplane.accidents.base.model.TopAccident;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccidentConsumer {

    private static final Logger logger = LoggerFactory.getLogger(AccidentConsumer.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "${app.kafka.topics.accidents.topN}", groupId = "consumer-2", concurrency = "1",
            containerFactory = "multiTypeKafkaListenerContainerFactory")
    public void listen(ConsumerRecord<String, String> consumerRecord) throws JsonProcessingException {
        List<TopAccident> topAccidents = objectMapper
                .readValue(consumerRecord.value(), new TypeReference<>(){});

        topAccidents.forEach(
                System.out::println
        );
    }
}
