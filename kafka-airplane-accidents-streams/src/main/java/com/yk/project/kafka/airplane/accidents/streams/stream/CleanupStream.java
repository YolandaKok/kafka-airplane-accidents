package com.yk.project.kafka.airplane.accidents.streams.stream;

import com.yk.project.kafka.airplane.accidents.base.model.Accident;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerde;

@Configuration
public class CleanupStream {
    @Value("${app.kafka.topics.raw}")
    private String rawTopic;

    @Value("${app.kafka.topics.cleanup}")
    private String cleanupTopic;

    @Bean
    public KStream<Long, Accident> streamFilterEmptySpeciesNameAndQuantity(StreamsBuilder builder) {
        var accidentSerde = new JsonSerde<>(Accident.class);

        var cleanUpStream = builder.stream(rawTopic, Consumed.with(Serdes.Long(), accidentSerde))
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
}
