package com.yk.project.kafka.airplane.accidents.producer.service;

import com.yk.project.kafka.airplane.accidents.base.csvparser.CsvParser;
import com.yk.project.kafka.airplane.accidents.base.model.Accident;
import com.yk.project.kafka.airplane.accidents.producer.produce.KafkaProducer;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class ProducerService {

    private final CsvParser<Accident> csvParser = new CsvParser<>(Accident.class);

    @Autowired
    private final KafkaProducer kafkaProducer;

    @Value("${app.kafka.topics.raw}")
    private String rawTopic;

    private static final String path = "./data/accidents.csv";

    public ProducerService(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostConstruct
    public void produceAccidentRawRecords() {
        List<Accident> accidents = csvParser.readFile(path);
        System.out.println("Accidents: " + accidents.size());
        accidents.forEach(
                accident -> {
                    try {
                        kafkaProducer
                                .sendMessageWithKeyAsync(rawTopic, accident.getRecordId(), accident);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }
}
