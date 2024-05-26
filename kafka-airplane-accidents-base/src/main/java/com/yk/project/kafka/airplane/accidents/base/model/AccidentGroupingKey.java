package com.yk.project.kafka.airplane.accidents.base.model;

import com.univocity.parsers.annotations.Parsed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AccidentGroupingKey {
    private Integer incidentYear;
    private Integer incidentMonth;
    private String speciesName;
}
