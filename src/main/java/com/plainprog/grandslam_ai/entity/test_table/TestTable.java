package com.plainprog.grandslam_ai.entity.test_table;

import jakarta.persistence.*;

@Entity
@Table(name = "test")
public class TestTable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "str")
    private String str;

    public TestTable() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
