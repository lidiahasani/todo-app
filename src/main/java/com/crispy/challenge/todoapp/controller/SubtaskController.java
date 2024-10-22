package com.crispy.challenge.todoapp.controller;

import com.crispy.challenge.todoapp.dto.SubtaskDto;
import com.crispy.challenge.todoapp.service.SubtaskService;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubtaskDto createTask(@RequestBody SubtaskDto subtaskDto, @RequestParam Long taskId) {
        logger.debug("Creating subtask {}.", subtaskDto);
        var result = subtaskService.createSubtask(subtaskDto, taskId);
        logger.debug("Created subtask {}", result);
        return result;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SubtaskDto viewDetails(@PathVariable Long id) {
        logger.debug("Retrieving subtask by task id {}.", id);
        var result = subtaskService.getSubtaskDetails(id);
        logger.debug("Retrieved subtask: {}", result);
        return result;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SubtaskDto updateTask(@PathVariable Long id, @RequestBody SubtaskDto subtaskDto) {
        logger.debug("Updating subtask {} with {}.", id, subtaskDto);
        var result = subtaskService.updateSubtask(id, subtaskDto);
        logger.debug("Updated subtask: {}", result);
        return result;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubtask(@PathVariable Long id) {
        logger.debug("Deleting subtask with id: {}.", id);
        subtaskService.deleteSubtask(id);
        logger.debug("Successfully deleted subtask.");
    }
}
