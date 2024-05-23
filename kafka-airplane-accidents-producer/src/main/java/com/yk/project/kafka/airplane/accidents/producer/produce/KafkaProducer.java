package com.yk.project.kafka.airplane.accidents.producer.produce;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    @Autowired
    private final KafkaTemplate<Long, Object> kafkaTemplate;

    public void sendMessageWithKeyAsync(final String topic, final Long key, final Object value) {
        kafkaTemplate.send(topic, key, value);
    }
}