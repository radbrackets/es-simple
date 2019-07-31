package com.radbrackets.ar;

import java.util.*;

import static com.radbrackets.store.MetadataItem.*;
import static java.util.stream.Stream.of;

public class UnitOfWork {

    public static final String CORRELATION_ID = "correlation-id";
    public static final String CAUSATION_ID = "causation-id";
    public static final String CAUSED_BY = "caused-by";
    private Optional<String> loggedIn;
    private Optional<String> correlationId;
    private Optional<String> causationId;


    public Map<String, String> metadata(){
        return of(item(CAUSED_BY, loggedIn), item(CORRELATION_ID, correlationId), item(CAUSATION_ID, causationId))
            .collect(HashMap::new, (meta, item) -> item.apply(meta), HashMap::putAll);
    }

}
