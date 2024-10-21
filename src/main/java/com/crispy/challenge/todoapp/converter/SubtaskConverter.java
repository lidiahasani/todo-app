package com.crispy.challenge.todoapp.converter;

import com.crispy.challenge.todoapp.dto.SubtaskDto;
import com.crispy.challenge.todoapp.model.Subtask;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SubtaskConverter {

    public List<SubtaskDto> toSubtaskDtoList(List<Subtask> subtasks) {
        return subtasks.stream()
                .map(this::toSubtaskDto)
                .toList();
    }

    public SubtaskDto toSubtaskDto(Subtask subtask) {
        return new SubtaskDto(subtask.getId(),
                subtask.getName(),
                subtask.getDescription(),
                subtask.getStatus().getValue());
    }

}
