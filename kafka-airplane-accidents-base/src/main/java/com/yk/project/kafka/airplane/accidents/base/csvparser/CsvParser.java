package com.yk.project.kafka.airplane.accidents.base.csvparser;

import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvRoutines;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CsvParser<T> {
  final Class<T> typeParameterClass;

  public CsvParser(Class<T> typeParameterClass) {
    this.typeParameterClass = typeParameterClass;
  }

  public List<T> readFile(String path) {
    try (Reader inputReader =
        new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8)) {
      BeanListProcessor<T> rowProcessor = new BeanListProcessor<>(typeParameterClass);
      CsvParserSettings settings = new CsvParserSettings();
      settings.setHeaderExtractionEnabled(true);
      settings.setProcessor(rowProcessor);
      com.univocity.parsers.csv.CsvParser parser =
          new com.univocity.parsers.csv.CsvParser(settings);
      parser.parse(inputReader);
      return rowProcessor.getBeans();
    } catch (IOException e) {
      log.error("Error while parsing file: {} and class type: {}", path, typeParameterClass);
    }
    return null;
  }

  public void writeToFile(List<T> results, String csvExportFilePath, String... headers) {
    CsvRoutines csvRoutines = new CsvRoutines();
    csvRoutines.writeAll(results, typeParameterClass, new File(csvExportFilePath), headers);
  }
}
