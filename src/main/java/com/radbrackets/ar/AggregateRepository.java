package com.radbrackets.ar;

import com.radbrackets.store.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AggregateRepository<ID, AGGREGATE extends AggregateRoot<ID>> {

    private final EventStore eventStore;
    private final Function<List<PersistentEvent>, AGGREGATE> creator;
    private final UnitOfWorkProvider unitOfWork;

    public AggregateRepository(EventStore eventStore, Function<List<PersistentEvent>, AGGREGATE> creator, UnitOfWorkProvider unitOfWork) {
        this.eventStore = eventStore;
        this.creator = creator;
        this.unitOfWork = unitOfWork;
    }

    public Optional<AGGREGATE> findBy(ID id){
        List<PersistentEvent> read = eventStore.read(id.toString(), -1L);
        if(read.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(creator.apply(read));
    }

    public void add(AGGREGATE aggregate){
        eventStore.append(
            aggregate.getId().toString(),
            aggregate.getExpectedVersion(),
            aggregate.getUncommittedEvents()
                .map(this::toEventInformation)
                .collect(Collectors.toList())
        );
    }

    private EventInformation toEventInformation(Event event) {
        return new EventInformation(UUID.randomUUID(), event, unitOfWork.current().metadata());
    }
}
