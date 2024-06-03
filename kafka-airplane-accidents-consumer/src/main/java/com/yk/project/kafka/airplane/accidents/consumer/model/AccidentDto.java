package com.yk.project.kafka.airplane.accidents.consumer.model;

import com.univocity.parsers.annotations.Parsed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class AccidentDto {
    @Parsed
    private Integer year;
    @Parsed
    private Integer ranking;
    @Parsed
    private String speciesName;
    @Parsed
    private Long count;
}
