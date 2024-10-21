package com.crispy.challenge.todoapp.controller;

import com.crispy.challenge.todoapp.dto.ProjectDetailsDto;
import com.crispy.challenge.todoapp.dto.ProjectDto;
import com.crispy.challenge.todoapp.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectDto createProject(@RequestBody ProjectDto projectDto, @RequestParam Long ownerId) {
        logger.info("Creating project: {}.", projectDto);
        var result = projectService.createProject(projectDto, ownerId);
        logger.info("Created project {}.", result);
        return result;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ProjectDto>> getAllProjects(@RequestParam Long ownerId) {
        logger.info("Retrieving projects by ownerId: {}.", ownerId);
        var result = projectService.getProjectsByOwner(ownerId);
        logger.info("Retrieved projects: {}.", result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/archived")
    public ResponseEntity<List<ProjectDto>> getArchivedProjects(@RequestParam Long ownerId) {
        logger.info("Retrieving archived projects by ownerId: {}.", ownerId);
        var result = projectService.getArchivedProjectsByOwner(ownerId);
        logger.info("Retrieved archived projects: {}.", result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDetailsDto> viewDetails(@PathVariable Long id) {
        logger.info("Retrieving project by projectId: {}.", id);
        var result = projectService.getProjectDetails(id);
        logger.info("Retrieved project: {}.", result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDto> updateProject(@PathVariable Long id, @RequestBody ProjectDto projectDto) {
        logger.info("Updating project: {}.", projectDto);
        var result = projectService.updateProject(id, projectDto);
        logger.info("Updated project: {}.", result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        logger.info("Deleting project with id: {}.", id);
        projectService.deleteProject(id);
        logger.info("Successfully deleted project.");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
