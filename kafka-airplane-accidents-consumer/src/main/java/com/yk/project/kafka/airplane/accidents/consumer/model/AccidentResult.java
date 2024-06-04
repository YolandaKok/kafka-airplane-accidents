package com.yk.project.kafka.airplane.accidents.consumer.model;

import com.univocity.parsers.annotations.Parsed;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("AccidentResult")
@Builder
@Data
public class AccidentResult implements Serializable {
    private String id;
    private String speciesName;
    private Long count;
}
