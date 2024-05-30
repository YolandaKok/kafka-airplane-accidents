package com.yk.project.kafka.airplane.accidents.consumer.consume;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class AccidentConsumer {

    private static final Logger logger = LoggerFactory.getLogger(AccidentConsumer.class);

    @KafkaListener(topics = "${app.kafka.topics.accidents.topN}", concurrency = "1",
            containerFactory = "multiTypeKafkaListenerContainerFactory")
    public void listen(ConsumerRecord<String, String> consumerRecord) {
        var items = Arrays.asList(consumerRecord.value().split(","));
        items.forEach(
                record -> {
                    var line = record.split("@");
                    System.out.println(line[0] + " " + line[1] + " " + line[2]);
                }
        );
    }
}
