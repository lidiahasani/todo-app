package com.crispy.challenge.todoapp.converter;

import com.crispy.challenge.todoapp.model.Priority;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PriorityConverter {

    public Priority convert(String priority) {
        return Objects.nonNull(priority) ? Priority.fromValue(priority) : Priority.LOW;
    }
}
