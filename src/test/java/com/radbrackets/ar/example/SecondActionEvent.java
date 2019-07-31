package com.radbrackets.ar.example;

import com.radbrackets.ar.Event;

import java.util.*;

public class SecondActionEvent extends Event<String> {
    private Integer test2;

    public SecondActionEvent(String id, Integer test2) {
        super(id);
        this.test2 = test2;
    }

    public Integer getTest2() {
        return test2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SecondActionEvent)) return false;
        SecondActionEvent that = (SecondActionEvent) o;
        return Objects.equals(getTest2(), that.getTest2()) && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTest2(), getId());
    }
}
