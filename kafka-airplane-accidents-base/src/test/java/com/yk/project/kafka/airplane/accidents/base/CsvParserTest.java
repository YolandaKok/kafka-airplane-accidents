package com.yk.project.kafka.airplane.accidents.base;

import com.yk.project.kafka.airplane.accidents.base.csvparser.CsvParser;
import com.yk.project.kafka.airplane.accidents.base.model.Accident;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.List;

public class CsvParserTest {
    private final CsvParser<Accident> csvParser = new CsvParser<>(Accident.class);

    @Test
    public void test() {
        String path = "src/test/resources/accidents.csv";
        List<Accident> accidents = csvParser.readFile(path);

        Assertions.assertEquals("DELTA AIR LINES", accidents.getFirst().getOperator());
        Assertions.assertEquals("EUROPEAN STARLING", accidents.get(370).getSpeciesName());
        Assertions.assertEquals("USA", accidents.get(10).getOperatorId());
        Assertions.assertEquals(180, accidents.get(480).getSpeed());
    }
}
