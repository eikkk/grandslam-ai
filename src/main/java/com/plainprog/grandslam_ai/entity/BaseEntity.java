package com.plainprog.grandslam_ai.entity;

import java.io.Serializable;

public abstract class BaseEntity<ID_TYPE> implements Identifiable<ID_TYPE>, Serializable {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (this.getClass() != o.getClass()) return false;

        Identifiable<ID_TYPE> entity2 = (Identifiable<ID_TYPE>) o;
        ID_TYPE id1 = this.getId();
        ID_TYPE id2 = entity2.getId();

        return id1 != null && id1.equals(id2);
    }

    @Override
    public int hashCode() {
        ID_TYPE id = this.getId();
        return id != null ? id.hashCode() : System.identityHashCode(this);
    }
}