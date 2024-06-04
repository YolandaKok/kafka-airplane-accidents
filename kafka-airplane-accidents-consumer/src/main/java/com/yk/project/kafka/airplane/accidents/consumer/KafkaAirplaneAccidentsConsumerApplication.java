package com.yk.project.kafka.airplane.accidents.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
public class KafkaAirplaneAccidentsConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaAirplaneAccidentsConsumerApplication.class, args);
	}

}
