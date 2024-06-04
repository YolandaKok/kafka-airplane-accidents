package com.yk.project.kafka.airplane.accidents.consumer.redis.repository;

import com.yk.project.kafka.airplane.accidents.consumer.model.AccidentResult;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccidentResultRepository extends CrudRepository<AccidentResult, String> {}
