package com.crispy.challenge.todoapp.service;

import com.crispy.challenge.todoapp.dto.DailyTaskDto;
import com.crispy.challenge.todoapp.dto.TaskDetailsDto;
import com.crispy.challenge.todoapp.dto.TaskDto;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {
    TaskDto create(TaskDto taskDto, Long projectId);

    List<DailyTaskDto> getDailyTasks(Long ownerId, LocalDate date);

    TaskDetailsDto getTaskDetails(Long id);

    TaskDto updateTask(Long id, TaskDto taskDto);

    void deleteTask(Long id);
}
