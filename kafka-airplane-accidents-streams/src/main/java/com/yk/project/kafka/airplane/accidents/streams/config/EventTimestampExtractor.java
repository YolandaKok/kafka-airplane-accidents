package com.yk.project.kafka.airplane.accidents.streams.config;

import com.yk.project.kafka.airplane.accidents.base.model.Accident;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;


@Service
public class EventTimestampExtractor implements TimestampExtractor {

    private static final Logger log = LoggerFactory.getLogger(EventTimestampExtractor.class);

    @Override
    public long extract(ConsumerRecord<Object, Object> consumerRecord, long l) {
        final Accident event = (Accident) consumerRecord.value();
        final ZonedDateTime eventCreationTime = ZonedDateTime
                .of(event.getIncidentYear(), 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
        final long timestamp = eventCreationTime.toInstant().toEpochMilli();

        log.info("Event ({}) yielded timestamp: {}", event.getRecordId(), timestamp);

        return timestamp;
    }
}