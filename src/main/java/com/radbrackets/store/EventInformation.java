package com.radbrackets.store;

import com.radbrackets.ar.Event;

import java.util.*;

public class EventInformation {
    private UUID uuid;
    private Event event;
    private Map<String, String> metadata;

    public EventInformation(UUID uuid, Event event, Map<String, String> metadata) {
        this.uuid = uuid;
        this.event = event;
        this.metadata = metadata;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Event getEvent() {
        return event;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventInformation that = (EventInformation) o;
        return Objects.equals(uuid, that.uuid) &&
                Objects.equals(event, that.event) &&
                Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, event, metadata);
    }

    public String getType() {
        return event.getClass().getName();
    }
}
