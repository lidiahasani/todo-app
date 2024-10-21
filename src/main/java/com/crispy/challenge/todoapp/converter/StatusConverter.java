package com.crispy.challenge.todoapp.converter;

import com.crispy.challenge.todoapp.model.Status;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class StatusConverter {

    public Status convert(String status) {
        return Objects.nonNull(status) ? Status.fromValue(status) : Status.TO_DO;
    }
}
