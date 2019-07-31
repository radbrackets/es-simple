package com.radbrackets.ar.example;

import com.radbrackets.ar.*;
import com.radbrackets.store.PersistentEvent;

import java.util.List;

public class TestAggregate extends AggregateRoot<String> {

    private String test1;
    private Integer test2;
    private Double test3;

    public TestAggregate(String id) {
        apply(new AggregateCreated(id));
    }

    public TestAggregate(List<PersistentEvent> events) {
        super(events);
    }

    public void secondAction(Integer test2){
        apply(new SecondActionEvent(getId(),test2));
    }

    public void firstAction(String test1){
        apply(new FirstActionDone(getId(), test1));
    }

    public void thirdAction(Double test3){
        apply(new ThirdActionDone(getId(), test3));
    }

    public void doCompound(Integer math, Double velocity, String paper) {
        apply(new SecondActionEvent(getId(), math), new FirstActionDone(getId(), paper), new ThirdActionDone(getId(), velocity));
    }

    private void on(AggregateCreated created){
        id = created.getId();
    }

    private void on(SecondActionEvent secondActionDone){
        test2 = secondActionDone.getTest2();
    }

    private void on(ThirdActionDone thirdActionDone){
        test3 = thirdActionDone.getTest3();
    }

    @Apply
    private void on(FirstActionDone firstActionDone){
        test1 = firstActionDone.getTest1();
    }


    public String getTest1() {
        return test1;
    }

    public Integer getTest2() {
        return test2;
    }

    public Double getTest3() {
        return test3;
    }
}
