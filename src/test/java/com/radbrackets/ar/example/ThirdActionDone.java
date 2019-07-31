package com.radbrackets.ar.example;

import com.radbrackets.ar.Event;

import java.util.*;

public class ThirdActionDone extends Event<String> {
    private Double test3;

    public ThirdActionDone(String id, Double test3) {
        super(id);
        this.test3 = test3;
    }

    public Double getTest3() {
        return test3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ThirdActionDone)) return false;
        ThirdActionDone that = (ThirdActionDone) o;
        return Objects.equals(getTest3(), that.getTest3()) && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTest3(), getId());
    }
}
