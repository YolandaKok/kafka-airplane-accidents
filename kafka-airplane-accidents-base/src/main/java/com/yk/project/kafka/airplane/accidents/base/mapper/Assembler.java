package com.yk.project.kafka.airplane.accidents.base.mapper;

import java.util.List;

public interface Assembler<T, U> {
    public T assemble(U item);
    public List<T> assembleAll(List<U> items);
}
