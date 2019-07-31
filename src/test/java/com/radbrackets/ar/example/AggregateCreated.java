package com.radbrackets.ar.example;

import com.radbrackets.ar.Event;

import java.util.Objects;

public class AggregateCreated extends Event<String> {

    public AggregateCreated(String id) {
        super(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AggregateCreated)) return false;
        AggregateCreated created = (AggregateCreated) o;
        return Objects.equals(getId(), created.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
