package com.plainprog.grandslam_ai.entity;

import org.hibernate.Hibernate;

import java.io.Serializable;

public abstract class BaseEntity<ID_TYPE> implements Identifiable<ID_TYPE>, Serializable {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        // Get the real, unproxied class
        Class<?> thisClass = Hibernate.getClass(this);
        Class<?> otherClass = Hibernate.getClass(o);

        if (thisClass != otherClass) return false;

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