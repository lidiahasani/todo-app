package com.crispy.challenge.todoapp.dto;

import java.util.List;

public record ProjectDetailsDto(ProjectDto projectDto, List<TaskDto> taskDtoList) {
}
