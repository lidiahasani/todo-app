package com.crispy.challenge.todoapp.dto;

public record SubtaskDto(Long id,
                         String name,
                         String description,
                         String status,
                         Long taskId) {
}