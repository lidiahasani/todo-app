package com.crispy.challenge.todoapp.service.impl;

import com.crispy.challenge.todoapp.converter.PriorityConverter;
import com.crispy.challenge.todoapp.converter.StatusConverter;
import com.crispy.challenge.todoapp.converter.TaskConverter;
import com.crispy.challenge.todoapp.dto.*;
import com.crispy.challenge.todoapp.exception.NoResultFoundException;
import com.crispy.challenge.todoapp.model.Project;
import com.crispy.challenge.todoapp.model.Task;
import com.crispy.challenge.todoapp.repository.ProjectRepository;
import com.crispy.challenge.todoapp.repository.SubtaskRepository;
import com.crispy.challenge.todoapp.repository.TaskRepository;
import com.crispy.challenge.todoapp.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.UnaryOperator;

import static com.crispy.challenge.todoapp.security.OwnerUtils.getOwnerId;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskConverter taskConverter;
    private final ProjectRepository projectRepository;
    private final StatusConverter statusConverter;
    private final PriorityConverter priorityConverter;
    private final SubtaskRepository subtaskRepository;

    public TaskServiceImpl(TaskRepository taskRepository, TaskConverter taskConverter, ProjectRepository projectRepository, StatusConverter statusConverter, PriorityConverter priorityConverter, SubtaskRepository subtaskRepository) {
        this.taskRepository = taskRepository;
        this.taskConverter = taskConverter;
        this.projectRepository = projectRepository;
        this.statusConverter = statusConverter;
        this.priorityConverter = priorityConverter;
        this.subtaskRepository = subtaskRepository;
    }

    @Override
    public TaskDto createTask(TaskDto taskDto, Long projectId) {
        Task task = projectRepository.findByIdAndOwner(projectId, getOwnerId())
                .map(project -> taskConverter.toTask(taskDto, project))
                .orElseThrow(() -> new NoResultFoundException("Project not found"));
        task = taskRepository.save(task);
        return taskConverter.toTaskDto(task);
    }

    @Override
    public List<DailyTaskDto> getDailyTasks(LocalDate date) {
        DayBoundsDto dayBoundsDto = convertToDayBounds(date);
        return taskRepository.findDailyTasks(getOwnerId(), dayBoundsDto.start(), dayBoundsDto.end());
    }

    private static DayBoundsDto convertToDayBounds(LocalDate date) {
        Instant start = date.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant end = start.plus(1, ChronoUnit.DAYS);
        return new DayBoundsDto(start, end);
    }

    @Override
    public TaskDetailsDto getTaskDetails(Long id) {
        return taskRepository.findTaskWithSubtasks(id, getOwnerId())
                .map(taskConverter::toTaskDetailsDto)
                .orElseThrow(() -> new NoResultFoundException("Task not found."));
    }

    @Override
    public TaskDto updateTask(Long id, TaskDto taskDto) {
        return taskRepository.findByIdAndOwner(id, getOwnerId())
                .map(updateTask(taskDto))
                .map(taskRepository::save)
                .map(taskConverter::toTaskDto)
                .orElseThrow(() -> new NoResultFoundException("Task not found."));
    }

    private UnaryOperator<Task> updateTask(TaskDto taskDto) {
        return task -> {
            task.setName(taskDto.name());
            task.setDescription(taskDto.description());
            task.setStatus(statusConverter.convert(taskDto.status()));
            task.setDueDate(taskDto.dueDate());
            task.setPriority(priorityConverter.convert(taskDto.priority()));
            Project project = projectRepository.findByIdAndOwner(taskDto.projectId(), getOwnerId())
                            .orElseThrow(() -> new NoResultFoundException("Project not found"));
            task.setProject(project);
            return task;
        };
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {
        taskRepository.softDeleteById(id, getOwnerId());
        subtaskRepository.softDeleteSubtasksByTaskId(id);
    }
}
