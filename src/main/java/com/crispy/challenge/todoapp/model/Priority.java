package com.crispy.challenge.todoapp.model;

import java.util.Arrays;

public enum Priority {
    LOW("LOW"),
    MEDIUM("MEDIUM"),
    HIGH("HIGH");

    private final String value;

    Priority(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Priority fromValue(String value) {
        return Arrays.stream(values())
                .filter(priority -> value.equalsIgnoreCase(priority.value))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Invalid argument for Priority"));
    }
}
