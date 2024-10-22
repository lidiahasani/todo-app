package com.crispy.challenge.todoapp.converter;

import com.crispy.challenge.todoapp.dto.SubtaskDto;
import com.crispy.challenge.todoapp.model.Subtask;
import com.crispy.challenge.todoapp.model.Task;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SubtaskConverter {

    private final StatusConverter statusConverter;

    public SubtaskConverter(StatusConverter statusConverter) {
        this.statusConverter = statusConverter;
    }

    public List<SubtaskDto> toSubtaskDtoList(List<Subtask> subtasks) {
        return subtasks.stream()
                .map(this::toSubtaskDto)
                .toList();
    }

    public SubtaskDto toSubtaskDto(Subtask subtask) {
        return new SubtaskDto(subtask.getId(),
                subtask.getName(),
                subtask.getDescription(),
                subtask.getStatus().getValue(),
                subtask.getTask().getId());
    }

    public Subtask toSubtask(SubtaskDto subtaskDto, Task task) {
        Subtask subtask = new Subtask();
        subtask.setName(subtaskDto.name());
        subtask.setDescription(subtaskDto.description());
        subtask.setStatus(statusConverter.convert(subtaskDto.status()));
        subtask.setTask(task);
        return subtask;
    }
}
