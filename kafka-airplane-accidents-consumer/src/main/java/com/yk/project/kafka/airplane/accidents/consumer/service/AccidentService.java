package com.yk.project.kafka.airplane.accidents.consumer.service;

import com.yk.project.kafka.airplane.accidents.consumer.model.AccidentResult;
import com.yk.project.kafka.airplane.accidents.consumer.redis.repository.AccidentResultRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccidentService {

  private final AccidentResultRepository accidentResultRepository;

  public List<AccidentResult> findAll() {
    var iterator = accidentResultRepository.findAll().iterator();
    List<AccidentResult> accidentResults = new ArrayList<>();
    while (iterator.hasNext()) {
      accidentResults.add(iterator.next());
    }

    return accidentResults.stream()
        .sorted(Comparator.comparing(AccidentResult::getId))
        .collect(Collectors.toList());
  }
}
