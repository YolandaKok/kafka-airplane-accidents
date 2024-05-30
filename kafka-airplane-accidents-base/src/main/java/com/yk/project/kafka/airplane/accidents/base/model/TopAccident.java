package com.yk.project.kafka.airplane.accidents.base.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class TopAccident {
    private Integer year;
    private String speciesName;
    private Long count;
}
