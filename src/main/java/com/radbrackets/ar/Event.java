package com.radbrackets.ar;

public abstract class Event<ID> {
    private ID id;

    public Event(ID id) {
        this.id = id;
    }

    public ID getId() {
        return id;
    }
}
