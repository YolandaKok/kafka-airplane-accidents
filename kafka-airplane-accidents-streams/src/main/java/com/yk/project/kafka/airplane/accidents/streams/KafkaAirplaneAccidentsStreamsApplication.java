package com.yk.project.kafka.airplane.accidents.streams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@SpringBootApplication
@EnableKafkaStreams
public class KafkaAirplaneAccidentsStreamsApplication {

  public static void main(String[] args) {
    SpringApplication.run(KafkaAirplaneAccidentsStreamsApplication.class, args);
  }
}
