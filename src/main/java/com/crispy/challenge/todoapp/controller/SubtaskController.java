package com.crispy.challenge.todoapp.controller;

import com.crispy.challenge.todoapp.dto.SubtaskDto;
import com.crispy.challenge.todoapp.service.SubtaskService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subtasks")
public class SubtaskController {

    private final Logger logger = LoggerFactory.getLogger(SubtaskController.class);

    private final SubtaskService subtaskService;

    public SubtaskController(SubtaskService subtaskService) {
        this.subtaskService = subtaskService;
    }

    @Operation(summary = "Create a subtask")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubtaskDto createSubtask(@RequestBody SubtaskDto subtaskDto, @RequestParam Long taskId) {
        logger.debug("Creating subtask {}.", subtaskDto);
        var result = subtaskService.createSubtask(subtaskDto, taskId);
        logger.debug("Created subtask {}", result);
        return result;
    }

    @Operation(summary = "View details of a subtask")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SubtaskDto viewDetails(@PathVariable Long id) {
        logger.debug("Retrieving subtask by task id {}.", id);
        var result = subtaskService.getSubtaskDetails(id);
        logger.debug("Retrieved subtask: {}", result);
        return result;
    }

    @Operation(summary = "Update a subtask")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SubtaskDto updateSubtask(@PathVariable Long id, @RequestBody SubtaskDto subtaskDto) {
        logger.debug("Updating subtask {} with {}.", id, subtaskDto);
        var result = subtaskService.updateSubtask(id, subtaskDto);
        logger.debug("Updated subtask: {}", result);
        return result;
    }

    @Operation(summary = "Archive a subtask")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubtask(@PathVariable Long id) {
        logger.debug("Deleting subtask with id: {}.", id);
        subtaskService.deleteSubtask(id);
        logger.debug("Successfully deleted subtask.");
    }
}
