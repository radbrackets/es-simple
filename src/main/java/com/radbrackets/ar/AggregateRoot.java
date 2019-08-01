package com.radbrackets.ar;

import com.radbrackets.store.PersistentEvent;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import static com.radbrackets.ar.EventMethod.*;

public class AggregateRoot<ID> {

    public static final Long NON_VERSION = -1L;

    private Map<Class<?>, List<EventMethod>> eventMethods = Stream.of(this.getClass().getDeclaredMethods())
            .filter(hasSingleParameter().and(hasEventParameter().and(hasAnnotationEventHandler().or(isOnMethodName()))))
            .map(method -> new EventMethod(method, this))
            .collect(Collectors.groupingBy(EventMethod::getEventType, Collectors.toList()));

    private Deque<Event<ID>> uncommittedEvents = new ArrayDeque<>();

    protected ID id;

    private Long version;

    public AggregateRoot(){
        this.version = NON_VERSION;
    }

    protected AggregateRoot(List<PersistentEvent> events){
        this.version = events.stream()
                .map(PersistentEvent::getVersion)
                .max(Comparator.naturalOrder())
                .orElse(NON_VERSION);
        applyHistory(events.stream()
                .filter(Objects::nonNull)
                .map(this::getEvent)
        );
    }

    private Event<ID> getEvent(PersistentEvent persistentEvent) {
        return (Event<ID>) persistentEvent.getEvent();
    }

    protected void apply(Event<ID>... event){
        apply(Stream.of(event));
    }

    protected void apply(Stream<Event<ID>> event){
        event.filter(Objects::nonNull).forEach(this::apply);
    }

    public Stream<Event<ID>> getUncommittedEvents(){
        return uncommittedEvents.stream();
    }

    public Long getVersion() {
        return version;
    }

    public void consumeEvents(Consumer<Event<ID>> consumer) {
        IntStream.range(0, uncommittedEvents.size())
                .mapToObj(index -> uncommittedEvents.pop())
                .collect(Collectors.toList())
                .forEach(consumer);
    }

    private void applyHistory(Event<ID> event){
        eventMethods.getOrDefault(event.getClass(), Collections.emptyList())
                .forEach(eventMethod -> eventMethod.invoke(event));
    }

    private void applyHistory(Stream<Event<ID>> events){
        events.forEach(this::applyHistory);
    }

    protected void apply(Event<ID> event){
        uncommittedEvents.add(event);
        applyHistory(event);
    }

    public Long getExpectedVersion() {
        return version + uncommittedEvents.size();
    }

    public ID getId() {
        return id;
    }
}
