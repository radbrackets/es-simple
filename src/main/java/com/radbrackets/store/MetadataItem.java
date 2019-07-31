package com.radbrackets.store;

import java.util.*;

public class MetadataItem {
    private String name;
    private Optional<String> value;

    public MetadataItem(String name, Optional<String> value) {
        this.name = name;
        this.value = value == null ? Optional.empty() : value;
    }

    public void apply(Map<String, String> metadata){
        value.ifPresent(value -> metadata.put(name, value));
    }

    public String getName() {
        return name;
    }

    public static MetadataItem item(String name, Optional<String> value){
        return new MetadataItem(name, value);
    }
}
