package com.radbrackets.ar.example;

import com.radbrackets.ar.Event;

import java.util.*;

public class FirstActionDone extends Event<String> {
    private String test1;

    public FirstActionDone(String id, String test1) {
        super(id);
        this.test1 = test1;
    }

    public String getTest1() {
        return test1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FirstActionDone)) return false;
        FirstActionDone firstActionDone = (FirstActionDone) o;
        return Objects.equals(getTest1(), firstActionDone.getTest1()) && Objects.equals(getId(), firstActionDone.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTest1(), getId());
    }
}
