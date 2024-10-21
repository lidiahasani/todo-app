package com.crispy.challenge.todoapp.converter;

import com.crispy.challenge.todoapp.dto.SubtaskDto;
import com.crispy.challenge.todoapp.dto.TaskDetailsDto;
import com.crispy.challenge.todoapp.dto.TaskDto;
import com.crispy.challenge.todoapp.model.Project;
import com.crispy.challenge.todoapp.model.Task;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskConverter {

    private final StatusConverter statusConverter;
    private final PriorityConverter priorityConverter;
    private final SubtaskConverter subtaskConverter;

    public TaskConverter(StatusConverter statusConverter, PriorityConverter priorityConverter, SubtaskConverter subtaskConverter) {
        this.statusConverter = statusConverter;
        this.priorityConverter = priorityConverter;
        this.subtaskConverter = subtaskConverter;
    }

    public List<TaskDto> toTaskDtoList(List<Task> tasks) {
        return tasks.stream()
                .map(this::toTaskDto)
                .toList();
    }

    public TaskDto toTaskDto(Task task) {
        return new TaskDto(task.getId(),
                task.getName(),
                task.getDescription(),
                task.getStatus().getValue(),
                task.getDueDate(),
                task.getPriority().getValue(),
                task.getProject().getId());
    }

    public Task toTask(TaskDto taskDto) {
        Task task = new Task();
        task.setName(taskDto.name());
        task.setDescription(taskDto.description());
        task.setStatus(statusConverter.convert(taskDto.status()));
        task.setDueDate(taskDto.dueDate());
        task.setPriority(priorityConverter.convert(taskDto.priority()));
        return task;
    }

    public Task toTask(TaskDto taskDto, Project project) {
        Task task = toTask(taskDto);
        task.setProject(project);
        return task;
    }

    public TaskDetailsDto toTaskDetailsDto(Task task) {
        TaskDto taskDto = toTaskDto(task);
        List<SubtaskDto> subtaskDtoList = subtaskConverter.toSubtaskDtoList(task.getSubtasks());
        return new TaskDetailsDto(taskDto, subtaskDtoList, task.getDaysUntilDue());
    }

}
