package com.yk.project.kafka.airplane.accidents.consumer.mapper;

import com.yk.project.kafka.airplane.accidents.base.mapper.Assembler;
import com.yk.project.kafka.airplane.accidents.consumer.model.AccidentDto;
import com.yk.project.kafka.airplane.accidents.consumer.model.AccidentResult;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccidentMapper implements Assembler<AccidentDto, AccidentResult> {
  @Override
  public AccidentDto assemble(AccidentResult item) {
    return AccidentDto.builder()
        .year(Integer.valueOf(item.getId().split(":")[0]))
        .ranking(Integer.parseInt(item.getId().split(":")[1]) + 1)
        .count(item.getCount())
        .speciesName(item.getSpeciesName())
        .build();
  }

  @Override
  public List<AccidentDto> assembleAll(List<AccidentResult> items) {
    return items.stream().map(this::assemble).toList();
  }
}
