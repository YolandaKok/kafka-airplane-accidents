package com.yk.project.kafka.airplane.accidents.base.model;

import com.univocity.parsers.annotations.Parsed;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Accident {
    @Parsed(field = "Record ID")
    private Long recordId;

    @Parsed(field = "Incident Year")
    private Integer incidentYear;

    @Parsed(field = "Incident Month")
    private Integer incidentMonth;

    @Parsed(field = "Incident Day")
    private Integer incidentDay;

    @Parsed(field = "Operator ID")
    private String operatorId;

    @Parsed(field = "Operator")
    private String operator;

    @Parsed(field = "Aircraft")
    private String aircraft;

    @Parsed(field = "Aircraft Type")
    private String aircraftType;

    @Parsed(field = "Aircraft Make")
    private String aircraftMake;

    @Parsed(field = "Aircraft Model")
    private String aircraftModel;

    @Parsed(field = "Aircraft Mass")
    private String aircraftMass;

    @Parsed(field = "Engine Make")
    private String engineMake;

    @Parsed(field = "Engine Model")
    private String engineModel;

    @Parsed(field = "Engine Type")
    private String engineType;

    @Parsed(field = "Engine1 Position")
    private String engine1Position;

    @Parsed(field = "Engine2 Position")
    private String engine2Position;

    @Parsed(field = "Engine3 Position")
    private String engine3Position;

    @Parsed(field = "Engine4 Position")
    private String engine4Position;

    @Parsed(field = "Airport ID")
    private String airportId;

    @Parsed(field = "Airport")
    private String airport;

    @Parsed(field = "State")
    private String state;

    @Parsed(field = "Flight Phase")
    private String flightPhase;

    @Parsed(field = "Visibility")
    private String visibility;

    @Parsed(field = "Precipitation")
    private String precipitation;

    @Parsed(field = "Height")
    private Integer height;

    @Parsed(field = "Speed")
    private Integer speed;

    @Parsed(field = "Distance")
    private Double distance;

    @Parsed(field = "Species ID")
    private String speciesId;

    @Parsed(field = "Species Name")
    private String speciesName;

    @Parsed(field = "Species Quantity")
    private String speciesQuantity;

    @Parsed(field = "Flight Impact")
    private String flightImpact;

    @Parsed(field = "Fatalities")
    private Integer fatalities;

    @Parsed(field = "Injuries")
    private Integer injuries;
}
