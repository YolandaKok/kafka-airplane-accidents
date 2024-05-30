package com.yk.project.kafka.airplane.accidents.streams.stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yk.project.kafka.airplane.accidents.base.model.Accident;
import com.yk.project.kafka.airplane.accidents.base.model.AccidentGroupingKey;
import com.yk.project.kafka.airplane.accidents.base.model.GenericRecord;
import com.yk.project.kafka.airplane.accidents.streams.utils.PriorityQueueSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

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
//        cleanUpStream.print(Printed.toSysOut());

        return cleanUpStream;
    }

//    @Bean
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

    @Bean
    public KTable<Windowed<String>, String> sumAccidents(StreamsBuilder builder) {
        var accidentSerde = new JsonSerde<>(Accident.class);
        var windowSerde = WindowedSerdes.timeWindowedSerdeFrom(String.class, Duration.ofDays(365).toMillis());

        var accidentStream = builder.stream(cleanupTopic, Consumed.with(Serdes.Long(), accidentSerde))
                .selectKey((k, v) -> v.getSpeciesName() + "@" + v.getIncidentYear()
                ).groupByKey()
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofDays(365)))
                .count();

        final Comparator<GenericRecord> comparator =
                (o1, o2) -> (int) (o2.getCount() - o1.getCount());

        final KTable<Windowed<String>, PriorityQueue<GenericRecord>> allViewCounts = accidentStream
                .groupBy(
                        // the selector
                        (windowedArticle, count) -> {
                            // project on the industry field for key
                            final Windowed<String> windowedIndustry =
                                    new Windowed<>(windowedArticle.key().split("@")[1],
                                            windowedArticle.window());

                            final GenericRecord genericRecord = GenericRecord.builder()
                                    .count(count)
                                    .name(windowedArticle.key())
                                    .build();

                            return new KeyValue<>(windowedIndustry, genericRecord);
                        },
                        Grouped.with(windowSerde, new JsonSerde<>(GenericRecord.class))
                ).aggregate(
                        // the initializer
                        () -> new PriorityQueue<>(comparator),

                        // the "add" aggregator
                        (windowedIndustry, record, queue) -> {
                            queue.add(record);
                            return queue;
                        },

                        // the "remove" aggregator
                        (windowedIndustry, record, queue) -> {
                            queue.remove(record);
                            return queue;
                        },

                        Materialized.with(windowSerde, new PriorityQueueSerde<>(comparator, new JsonSerde<>(GenericRecord.class)))
                );

        final int topN = 5;
        final KTable<Windowed<String>, String> topViewCounts = allViewCounts
                .mapValues(queue -> {
                    final List<String> results = new ArrayList<>();
                    for (int i = 0; i < topN; i++) {
                        final GenericRecord record = queue.poll();
                        if (record == null) {
                            break;
                        }
                        results.add(record.getCount() + "@" + record.getName());
                    }
                    return String.join(",", results);
                });

        topViewCounts.toStream().print(Printed.toSysOut());

        topViewCounts.toStream().to("sliding-window-result",
                Produced.with(windowSerde, Serdes.String()));

        topViewCounts.toStream()
                .toTable(Named.as("sliding-window-result"));

        return topViewCounts;
    }

//    @Bean
    public KTable<AccidentGroupingKey, Long> groupingCount(StreamsBuilder builder) {
        var accidentSerde = new JsonSerde<>(Accident.class);
        var keySerde = new JsonSerde<>(AccidentGroupingKey.class);

        var accidentStream = builder.stream(cleanupTopic, Consumed.with(Serdes.Long(), accidentSerde))
                .selectKey((k, v) -> AccidentGroupingKey.builder()
                                                .speciesName(v.getSpeciesName())
                                                .incidentYear(v.getIncidentYear())
                                                .incidentMonth(v.getIncidentMonth())
                                                .build()
                ).groupByKey()
                .count(Materialized.with(keySerde, Serdes.Long()));

        accidentStream.toStream()
                .to("count-per-year-month-species", Produced.with(keySerde, Serdes.Long()));

        return accidentStream;
    }
}
