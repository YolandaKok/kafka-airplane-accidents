package com.yk.project.kafka.airplane.accidents.base.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class GenericRecord {
    private String name;
    private Long count;
}
