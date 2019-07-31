package com.radbrackets.ar;

import com.radbrackets.store.*;

import java.util.*;
import java.util.stream.*;

import static com.radbrackets.ar.StreamUtil.zip;
import static java.util.Arrays.asList;
import static java.util.stream.LongStream.range;

public class AssertionEventStore implements EventStore {

    private Map<String, Deque<PersistentEvent>> all = new HashMap<>();

    @Override
    public void append(String streamId, Long expectedVersion, List<EventInformation> events) {
        Deque<PersistentEvent> persistentEvents = all.computeIfAbsent(streamId, (nonUsed) -> new ArrayDeque<>());
        long lastVersion = persistentEvents.isEmpty() ? -1L : persistentEvents.getLast().getVersion();
        persistentEvents.addAll(
                zip(range(lastVersion + 1, expectedVersion + 1).boxed(), events.stream(), AbstractMap.SimpleEntry::new)
                    .map(entry -> toPersistentEvent(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList())
        );
    }

    private PersistentEvent toPersistentEvent(Long version, EventInformation eventInformation) {
        return new PersistentEvent(eventInformation.getUuid(), eventInformation.getEvent(), version, eventInformation.getMetadata());
    }

    @Override
    public List<PersistentEvent> read(String streamId, Long start) {
        return readInternal(streamId, start).collect(Collectors.toList());
    }

    @Override
    public List<PersistentEvent> read(String streamId, Long start, Integer limit) {
        return readInternal(streamId, start).limit(limit).collect(Collectors.toList());
    }

    private Stream<PersistentEvent> readInternal(String streamId, Long start){
        Deque<PersistentEvent> persistentEvents = all.computeIfAbsent(streamId, non -> new ArrayDeque<>());
        return persistentEvents.stream()
                .filter(event -> event.getVersion() >= start);
    }

    public void add(String streamId, PersistentEvent... events){
        Deque<PersistentEvent> persistentEvents = all.computeIfAbsent(streamId, non -> new ArrayDeque<>());
        persistentEvents.addAll(asList(events));
    }

    public ListAssertion<PersistentEvent, PersistentEventAssertion> forStream(String streamId){
        return new ListAssertion<>(new ArrayList<>(all.computeIfAbsent(streamId, non -> new ArrayDeque<>())), list -> event -> new PersistentEventAssertion(event, list));
    }
}
