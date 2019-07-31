package com.radbrackets.ar;

import org.assertj.core.api.Assertions;

import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class ListAssertion<TARGET, ASSERTION extends Backward> {

    protected List<TARGET> stores;
    private Function<TARGET, ASSERTION> toAssertion;
    private Integer position;


    public ListAssertion(List<TARGET> stores, Function<ListAssertion<TARGET, ASSERTION>, Function<TARGET, ASSERTION>> toAssertion) {
        this.stores = stores;
        this.toAssertion = toAssertion.apply(this);
    }

    public ListAssertion<TARGET, ASSERTION> isNotEmpty() {
        assertThat(stores).isNotEmpty();
        return this;
    }

    public ListAssertion<TARGET, ASSERTION> hasSize(int expected) {
        assertThat(stores).hasSize(expected);
        return this;
    }

    @SafeVarargs
    public final ListAssertion<TARGET, ASSERTION> contains(TARGET... expected){
        Assertions.assertThat(stores).contains(expected);
        return this;
    }

    @SafeVarargs
    public final ListAssertion<TARGET, ASSERTION> containsExactly(TARGET... expected){
        Assertions.assertThat(stores).containsExactly(expected);
        return this;
    }

    public ListAssertion<TARGET, ASSERTION> isEmpty() {
        assertThat(stores).isEmpty();
        return this;
    }

    public ListAssertion<TARGET, ASSERTION> tail(Integer start) {
        return new ListAssertion<>(stores.subList(start, stores.size() - 1), list -> toAssertion);
    }

    public ListAssertion<TARGET, ASSERTION> head(Integer end) {
        return new ListAssertion<>(stores.subList(0, end), list -> toAssertion);
    }

    public ASSERTION first(){
        return at(0);
    }

    public ASSERTION at(Integer position){
        this.position = position;
        return toAssertion.apply(stores.get(position));
    }

    public ASSERTION next(){
        return at(position+1);
    }

    public ASSERTION prev(){
        return at(position-1);
    }

    public ASSERTION single(){
        if(stores.size() != 1){
            throw new IllegalArgumentException("Single can be invoke only if ListAssertion contain single element");
        }
        return toAssertion.apply(stores.get(0));
    }

    public ASSERTION last() {
        return at(stores.size() - 1);
    }
}
