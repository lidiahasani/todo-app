package com.crispy.challenge.todoapp.controller;

import com.crispy.challenge.todoapp.dto.ProjectDetailsDto;
import com.crispy.challenge.todoapp.dto.ProjectDto;
import com.crispy.challenge.todoapp.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
    public ProjectDto createProject(@RequestBody ProjectDto projectDto) {
        logger.debug("Creating project: {}.", projectDto);
        var result = projectService.createProject(projectDto);
        logger.debug("Created project {}.", result);
        return result;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProjectDto> getAllProjects() {
        logger.debug("Retrieving projects.");
        var result = projectService.getProjectsByOwner();
        logger.debug("Retrieved projects: {}.", result);
        return result;
    }

    @GetMapping("/archived")
    @ResponseStatus(HttpStatus.OK)
    public List<ProjectDto> getArchivedProjects() {
        logger.debug("Retrieving archived projects.");
        var result = projectService.getArchivedProjectsByOwner();
        logger.debug("Retrieved archived projects: {}.", result);
        return result;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectDetailsDto viewDetails(@PathVariable Long id) {
        logger.debug("Retrieving project by project id {}.", id);
        var result = projectService.getProjectDetails(id);
        logger.debug("Retrieved project: {}.", result);
        return result;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectDto updateProject(@PathVariable Long id, @RequestBody ProjectDto projectDto) {
        logger.debug("Updating project {} with {}.", id, projectDto);
        var result = projectService.updateProject(id, projectDto);
        logger.debug("Updated project: {}.", result);
        return result;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject(@PathVariable Long id) {
        logger.debug("Deleting project with id: {}.", id);
        projectService.deleteProject(id);
        logger.debug("Successfully deleted project.");
    }

}
