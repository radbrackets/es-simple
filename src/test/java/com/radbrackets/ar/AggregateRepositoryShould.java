package com.radbrackets.ar;

import com.radbrackets.ar.example.*;
import com.radbrackets.store.PersistentEvent;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

class AggregateRepositoryShould {

     private static final String STREAM_ID = "123";
     private static final int EXPECTED_VALUE = 10;
     private static final String TEST_1 = "TEST_1";
     private AssertionEventStore eventStore = new AssertionEventStore();
     private AggregateRepository<String, TestAggregate> repository = new AggregateRepository<>(eventStore, TestAggregate::new, UnitOfWork::new);

     @Test
     void notFoundAggregateWhenEventsForGivenStreamIdNotExists(){
          //given
          String nonExistingId = "NON_EXISTING_ID";

          //when
          Optional<TestAggregate> nonExistingAggregate = repository.findBy(nonExistingId);

          //then
          assertThat(nonExistingAggregate).isEmpty();
     }

     @Test
     void createAggregateRootByApplyingEvents(){
          //given
          eventStore.add(STREAM_ID, new PersistentEvent(UUID.randomUUID(), new SecondActionEvent(STREAM_ID, EXPECTED_VALUE), 0L, new HashMap<>()));

          //when
          Optional<TestAggregate> aggregate = repository.findBy(STREAM_ID);

          //then
          assertThat(aggregate)
                  .hasValueSatisfying((test) -> assertThat(test.getTest2()).isEqualTo(EXPECTED_VALUE));
     }


     @Test
     void appendNewUncommittedEventToEventStoreInRightOrder(){
          //given
          TestAggregate aggregate = new TestAggregate(STREAM_ID);
          aggregate.firstAction(TEST_1);

          //when
          repository.add(aggregate);

          //then
          eventStore.forStream(STREAM_ID)
                  .hasSize(2)
                  .first()
                    .isFor(new AggregateCreated(STREAM_ID))
                    .back()
                  .next()
                    .isFor(new FirstActionDone(STREAM_ID, TEST_1));
     }

}
