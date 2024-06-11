package com.yk.project.kafka.airplane.accidents.consumer.model;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("AccidentResult")
@Builder
@Data
public class AccidentResult implements Serializable {
  private String id;
  private String speciesName;
  private Long count;
}
