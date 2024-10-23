package com.crispy.challenge.todoapp.dto;

import java.util.List;

public record TaskDetailsDto(TaskDto taskDto,
                             List<SubtaskDto> subtaskDtoList,
                             Long daysUntilDue) {
}
