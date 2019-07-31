package com.radbrackets.ar;

import com.radbrackets.store.PersistentEvent;
import org.assertj.core.api.Assertions;

import static com.radbrackets.ar.UnitOfWork.*;

public class PersistentEventAssertion implements Backward<PersistentEvent, PersistentEventAssertion>{

    private final PersistentEvent asserted;
    private ListAssertion<PersistentEvent, PersistentEventAssertion> list;

    public PersistentEventAssertion(PersistentEvent asserted, ListAssertion<PersistentEvent, PersistentEventAssertion> list) {
        this.asserted = asserted;
        this.list = list;
    }

    public PersistentEventAssertion hasVersion(Long expected){
        Assertions.assertThat(asserted.getVersion()).isEqualTo(expected);
        return this;
    }

    public PersistentEventAssertion isFor(Event expected){
        Assertions.assertThat(asserted.getEvent()).isEqualTo(expected);
        return this;
    }

    public PersistentEventAssertion hasMetadata(String key, String value){
        Assertions.assertThat(asserted.getMetadata()).containsEntry(key, value);
        return this;
    }

    public PersistentEventAssertion hasCorrelationId(String expectedCorrelationId){
        return hasMetadata(CORRELATION_ID, expectedCorrelationId);
    }

    public PersistentEventAssertion hasCausationId(String expectedCausationId){
        return hasMetadata(CAUSATION_ID, expectedCausationId);
    }

    @Override
    public ListAssertion<PersistentEvent, PersistentEventAssertion> back() {
        return list;
    }
}
