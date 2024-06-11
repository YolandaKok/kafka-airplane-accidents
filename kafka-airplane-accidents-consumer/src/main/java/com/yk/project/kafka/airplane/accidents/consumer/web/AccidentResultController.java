package com.yk.project.kafka.airplane.accidents.consumer.web;

import com.yk.project.kafka.airplane.accidents.base.csvparser.CsvParser;
import com.yk.project.kafka.airplane.accidents.consumer.mapper.AccidentMapper;
import com.yk.project.kafka.airplane.accidents.consumer.model.AccidentDto;
import com.yk.project.kafka.airplane.accidents.consumer.service.AccidentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/results")
public class AccidentResultController {
  @Value("${csv.export.file.path}")
  private String csvExportFilePath;

  private final CsvParser<AccidentDto> csvParser = new CsvParser<>(AccidentDto.class);

  private final AccidentService accidentService;

  private final AccidentMapper accidentMapper;

  public AccidentResultController(AccidentService accidentService, AccidentMapper accidentMapper) {
    this.accidentService = accidentService;
    this.accidentMapper = accidentMapper;
  }

  @GetMapping("/export")
  public void getResults() {
    var results = accidentMapper.assembleAll(accidentService.findAll());
    csvParser.writeToFile(results, csvExportFilePath, "year", "ranking", "speciesName", "count");
  }
}
