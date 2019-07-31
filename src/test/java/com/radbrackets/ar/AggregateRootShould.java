package com.radbrackets.ar;


import com.radbrackets.ar.example.*;
import com.radbrackets.store.PersistentEvent;
import org.junit.jupiter.api.*;

import java.util.*;
import java.util.stream.*;

import static com.radbrackets.ar.AggregateRoot.NON_VERSION;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class AggregateRootShould {

    public static final String ID = "123123";
    public static final int TEST_2 = 20;
    public static final double TEST_3 = 2.0;
    public static final String TEST_1 = "TEST_1";

    @Test
    void invokeMethodWithInheritanceEventParameter() {
        //given
        TestAggregate aggregate = new TestAggregate(ID);

        //when
        aggregate.secondAction(TEST_2);

        //then
        assertThat(aggregate.getTest2())
                .isEqualTo(TEST_2);
    }

    @Test
    void invokeMethodWithApplyAnnotation() throws Exception {
        //given
        TestAggregate aggregate = new TestAggregate( ID);

        //when
        aggregate.firstAction(AggregateRootShould.TEST_1);

        //then
        assertThat(aggregate.getTest1())
                .isEqualTo(TEST_1);
    }

    @Test
    void invokeMethodWithApplyName() throws Exception {
        //given
        TestAggregate aggregate = new TestAggregate(ID);

        //when
        aggregate.thirdAction(TEST_3);

        //then
        assertThat(aggregate.getTest3())
                .isEqualTo(TEST_3);
    }

    @Test
    public void restoreStateFromEvents() throws Exception {
        //given
        List<Event> events = Arrays.asList(new SecondActionEvent(ID, TEST_2), new ThirdActionDone(ID, TEST_3), new FirstActionDone(ID, TEST_1));
        List<PersistentEvent> loadedEvents = IntStream.range(0, events.size())
                .mapToObj(index -> new PersistentEvent(UUID.randomUUID(), events.get(index), (long) index, new HashMap<>()))
                .collect(toList());

        //when
        TestAggregate aggregate = new TestAggregate(loadedEvents);

        //then
        assertThat(aggregate.getTest3())
                .isEqualTo(TEST_3);
        assertThat(aggregate.getTest2())
                .isEqualTo(TEST_2);
        assertThat(aggregate.getTest1())
                .isEqualTo(TEST_1);
    }


    @Test
    void afterConsumeThanUncommittedEventsEventBeEmpty() {
        //given
        List<Event> events =  Arrays.asList(new SecondActionEvent(ID, 20), new ThirdActionDone(ID,2.0), new FirstActionDone(ID,"TEST_2"));
        List<PersistentEvent> loadedEvents = IntStream.range(0, events.size())
                .mapToObj(index -> new PersistentEvent(UUID.randomUUID(), events.get(index), (long) index, new HashMap<>()))
                .collect(toList());
        TestAggregate aggregate = new TestAggregate(loadedEvents);

        //when
        aggregate.consumeEvents((event) -> {});

        //then
        assertThat(aggregate.getUncommittedEvents()).isEmpty();
    }

    @Test
    void callsConsumerDuringConsumingEvents() {
        //given
        TestAggregate aggregate = new TestAggregate(ID);
        aggregate.thirdAction(90.0d);
        aggregate.secondAction(20);
        aggregate.firstAction("test-paper");
        ArrayList<Event> consumed = new ArrayList<>();

        //when
        aggregate.consumeEvents(consumed::add);

        //then
        assertThat(consumed).containsSequence(new ThirdActionDone(ID,90.0d),new SecondActionEvent(ID,20), new FirstActionDone(ID,"test-paper"));
    }

    @Test
    void shouldPosesVersionFromLastEvent() {
        //given
        List<Event> events =  Arrays.asList(new SecondActionEvent(ID,20), new ThirdActionDone(ID,2.0), new FirstActionDone(ID,"TEST_2"));
        List<PersistentEvent> loadedEvents = IntStream.range(0, events.size())
                .mapToObj(index -> new PersistentEvent(UUID.randomUUID(), events.get(index), (long) index, new HashMap<>()))
                .collect(toList());
        TestAggregate aggregate = new TestAggregate(loadedEvents);

        //when
        Long version = aggregate.getVersion();

        //then
        assertThat(version).isEqualTo(2L);
    }

    @Test
    void haveNonVersionMarkerWhenAggregateIsJustCreated() {
        //given
        TestAggregate aggregate = new TestAggregate("123");

        //when
        Long version = aggregate.getVersion();

        //then
        assertThat(version).isEqualTo(NON_VERSION);
    }

    @Test
    void consumeMultiplyEventNoOrderGuarantee() {
        //given
        TestAggregate aggregate = new TestAggregate(ID);
        aggregate.doCompound(20, 90.0d, "test-paper");
        ArrayList<Event> consumed = new ArrayList<>();

        //when
        aggregate.consumeEvents(consumed::add);

        //then
        assertThat(consumed).containsExactlyInAnyOrder(new AggregateCreated(ID), new ThirdActionDone(ID,90.0d),new SecondActionEvent(ID,20), new FirstActionDone(ID,"test-paper"));
    }

}
