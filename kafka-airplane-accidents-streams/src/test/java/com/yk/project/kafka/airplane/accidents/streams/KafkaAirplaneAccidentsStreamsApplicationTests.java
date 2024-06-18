package com.yk.project.kafka.airplane.accidents.streams;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaAdmin;

@SpringBootTest
class KafkaAirplaneAccidentsStreamsApplicationTests {

  @MockBean private KafkaAdmin kafkaAdmin;

  @Test
  void contextLoads() {}
}
