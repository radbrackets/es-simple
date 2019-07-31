package com.radbrackets.ar;

public interface Backward<TARGET, ASSERTION extends Backward<TARGET, ASSERTION>> {
    ListAssertion<TARGET, ASSERTION> back();
}
