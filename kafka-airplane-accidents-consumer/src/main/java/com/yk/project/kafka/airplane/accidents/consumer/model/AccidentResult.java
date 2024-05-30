package com.yk.project.kafka.airplane.accidents.consumer.model;

import lombok.Builder;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("AccidentResult")
@Builder
public class AccidentResult implements Serializable {
    private String id;
    private String speciesName;
    private Long count;
}
