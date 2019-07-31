package com.radbrackets.store;

import com.radbrackets.ar.Event;

import java.util.*;

public class PersistentEvent {

    private final UUID uuid;
    private final Event event;
    private final Long version;
    private final Map<String, String> metadata;

    public PersistentEvent(UUID uuid, Event event, Long version, Map<String, String> metadata) {
        this.uuid = uuid;
        this.event = event;
        this.version = version;
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

    public Long getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersistentEvent that = (PersistentEvent) o;
        return Objects.equals(uuid, that.uuid) &&
                Objects.equals(event, that.event) &&
                Objects.equals(version, that.version) &&
                Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, event, version, metadata);
    }
}
