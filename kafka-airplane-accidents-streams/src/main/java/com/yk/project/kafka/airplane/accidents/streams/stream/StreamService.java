package com.yk.project.kafka.airplane.accidents.streams.stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yk.project.kafka.airplane.accidents.base.model.Accident;
import com.yk.project.kafka.airplane.accidents.base.model.AccidentGroupingKey;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerde;

@Configuration
public class StreamService {
    @Value("${app.kafka.topics.raw}")
    private String rawTopic;

    @Value("${app.kafka.topics.cleanup}")
    private String cleanupTopic;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public KStream<Long, Accident> streamFilterEmptySpeciesNameAndQuantity(StreamsBuilder builder) {
        var accidentSerde = new JsonSerde<>(Accident.class);

        var cleanUpStream = builder.stream(rawTopic, Consumed.with(Serdes.Long(), accidentSerde))
                .filter((k, v) -> v.getRecordId() != null)
                .filter((k, v) -> v.getSpeciesName() != null && !v.getSpeciesName().isBlank())
                .filter((k, v) -> v.getSpeciesQuantity()!= null && !v.getSpeciesQuantity().isBlank())
                .map((key, value) -> {
                    switch (value.getSpeciesQuantity()) {
                        case "2-10" -> value.setSpeciesQuantity("5");
                        case "11-100" -> value.setSpeciesQuantity("50");
                        case "Over 100" -> value.setSpeciesQuantity("500");
                    }
                    return new KeyValue<>(key, value);
                });

        cleanUpStream.to(cleanupTopic, Produced.with(Serdes.Long(), accidentSerde));
        cleanUpStream.print(Printed.toSysOut());

        return cleanUpStream;
    }

    @Bean
    public KTable<String, Long> streamAccidentsGroupByYearMonthAndSpecies(StreamsBuilder builder) {
        var accidentSerde = new JsonSerde<>(Accident.class);

        var accidentStream = builder.stream(cleanupTopic, Consumed.with(Serdes.Long(), accidentSerde))
        .selectKey((k, v) ->
                {
                    try {
                        return objectMapper.writeValueAsString(
                                AccidentGroupingKey.builder()
                                        .speciesName(v.getSpeciesName())
                                        .incidentYear(v.getIncidentYear())
                                        .incidentMonth(v.getIncidentMonth())
                                        .build()
                        );
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
        ).groupByKey().count();

        accidentStream.toStream()
                .to("count-per-year-month-species");

        return accidentStream;
    }
}
