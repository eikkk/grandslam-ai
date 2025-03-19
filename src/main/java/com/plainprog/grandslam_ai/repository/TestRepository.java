package com.plainprog.grandslam_ai.repository;

import com.plainprog.grandslam_ai.model.db.TestTable;
import org.springframework.data.repository.CrudRepository;

public interface TestRepository extends CrudRepository<TestTable, Integer> {
}
