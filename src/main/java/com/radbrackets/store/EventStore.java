package com.radbrackets.store;

import java.util.List;

public interface EventStore {
    void append(String streamId, Long expectedVersion, List<EventInformation> events);
    List<PersistentEvent> read(String streamId, Long start);
    List<PersistentEvent> read(String streamId, Long start, Integer limit);
}
