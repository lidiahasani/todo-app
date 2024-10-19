package com.crispy.challenge.todoapp.model;

import java.util.Arrays;

public enum Status {
    TO_DO("TO DO"),
    DONE("DONE");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Status fromValue(String value) {
        return Arrays.stream(values())
                .filter(status -> value.equalsIgnoreCase(status.value))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Invalid argument for Status"));
    }
}
