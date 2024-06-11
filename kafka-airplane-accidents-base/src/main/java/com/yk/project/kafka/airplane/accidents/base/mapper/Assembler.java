package com.yk.project.kafka.airplane.accidents.base.mapper;

import java.util.List;

public interface Assembler<T, U> {
  T assemble(U item);
  List<T> assembleAll(List<U> items);
}
