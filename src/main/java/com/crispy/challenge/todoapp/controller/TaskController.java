package com.crispy.challenge.todoapp.controller;

import com.crispy.challenge.todoapp.dto.DailyTaskDto;
import com.crispy.challenge.todoapp.dto.TaskDetailsDto;
import com.crispy.challenge.todoapp.dto.TaskDto;
import com.crispy.challenge.todoapp.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Create a task")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto createTask(@RequestBody TaskDto taskDto, @RequestParam Long projectId) {
        logger.debug("Creating task {}.", taskDto);
        var result = taskService.createTask(taskDto, projectId);
        logger.debug("Created task {}", result);
        return result;
    }

    @Operation(summary = "Get tasks for a chosen day")
    @GetMapping("/daily")
    @ResponseStatus(HttpStatus.OK)
    public List<DailyTaskDto> getDailyTasks(@RequestParam LocalDate date) {
        logger.debug("Retrieving tasks for: {}.", date);
        var result = taskService.getDailyTasks(date);
        logger.debug("Retrieved tasks: {}", result);
        return result;
    }

    @Operation(summary = "View details of a task")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDetailsDto viewDetails(@PathVariable Long id) {
        logger.debug("Retrieving task by task id {}.", id);
        var result = taskService.getTaskDetails(id);
        logger.debug("Retrieved task: {}", result);
        return result;
    }

    @Operation(summary = "Update a task")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDto updateTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        logger.debug("Updating task {} with {}.", id, taskDto);
        var result = taskService.updateTask(id, taskDto);
        logger.debug("Updated task: {}", result);
        return result;
    }

    @Operation(summary = "Archive a task")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id) {
        logger.debug("Deleting task with id: {}.", id);
        taskService.deleteTask(id);
        logger.debug("Successfully deleted task.");
    }
}
