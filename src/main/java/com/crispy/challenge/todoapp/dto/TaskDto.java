package com.crispy.challenge.todoapp.dto;

import java.time.Instant;

public record TaskDto(Long id,
                      String name,
                      String description,
                      String status,
                      Instant dueDate,
                      String priority) {
}