package com.plainprog.grandslam_ai.service;

import com.plainprog.grandslam_ai.entity.test_table.TestTable;
import com.plainprog.grandslam_ai.entity.test_table.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    @Autowired
    TestRepository testRepository;

    public TestTable getTestTable() {
        return testRepository.findById(1).get();
    }
}
