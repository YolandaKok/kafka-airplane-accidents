package com.yk.project.kafka.airplane.accidents.consumer.consume;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yk.project.kafka.airplane.accidents.base.model.TopAccident;
import com.yk.project.kafka.airplane.accidents.consumer.model.AccidentResult;
import com.yk.project.kafka.airplane.accidents.consumer.redis.repository.AccidentResultRepository;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class AccidentConsumer {

    private static final Logger logger = LoggerFactory.getLogger(AccidentConsumer.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final AccidentResultRepository accidentResultRepository;

    @KafkaListener(topics = "${app.kafka.topics.accidents.topN}", groupId = "con-5", concurrency = "5",
            containerFactory = "multiTypeKafkaListenerContainerFactory")
    public void listen(ConsumerRecord<String, String> consumerRecord) throws JsonProcessingException {
        List<TopAccident> topAccidents = objectMapper
                .readValue(consumerRecord.value(), new TypeReference<>(){});

        List<AccidentResult> results = topAccidents.stream().map(
                item -> AccidentResult.builder()
                                .count(item.getCount())
                                .speciesName(item.getSpeciesName())
                                .id(item.getYear() + ":" + item.getRanking())
                                .build()
        ).toList();

        accidentResultRepository
                .saveAll(results);
    }
}
