package com.crispy.challenge.todoapp.controller;

import com.crispy.challenge.todoapp.dto.ProjectDetailsDto;
import com.crispy.challenge.todoapp.dto.ProjectDto;
import com.crispy.challenge.todoapp.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Create a project",
            description = """
                Create a new project. The request is a ProjectDto that contains name and status.
                Name must not be null, otherwise a Constraint Violation exception is thrown.
                If you include status, choose between 'TO DO' and 'DONE'. The default is 'TO DO'.
                Any other input for Status will produce an Illegal Argument exception.
                The response is a ProjectDto corresponding to the newly created Project object with id, name, and status.
                """)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectDto createProject(@RequestBody ProjectDto projectDto) {
        logger.debug("Creating project: {}.", projectDto);
        var result = projectService.createProject(projectDto);
        logger.debug("Created project {}.", result);
        return result;
    }

    @Operation(summary = "Get all projects")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProjectDto> getAllProjects() {
        logger.debug("Retrieving projects.");
        var result = projectService.getProjectsByOwner();
        logger.debug("Retrieved projects: {}.", result);
        return result;
    }

    @Operation(summary = "Get archived projects")
    @GetMapping("/archived")
    @ResponseStatus(HttpStatus.OK)
    public List<ProjectDto> getArchivedProjects() {
        logger.debug("Retrieving archived projects.");
        var result = projectService.getArchivedProjectsByOwner();
        logger.debug("Retrieved archived projects: {}.", result);
        return result;
    }

    @Operation(summary = "View details of a project")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectDetailsDto viewDetails(@PathVariable Long id) {
        logger.debug("Retrieving project by project id {}.", id);
        var result = projectService.getProjectDetails(id);
        logger.debug("Retrieved project: {}.", result);
        return result;
    }

    @Operation(summary = "Update a project")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectDto updateProject(@PathVariable Long id, @RequestBody ProjectDto projectDto) {
        logger.debug("Updating project {} with {}.", id, projectDto);
        var result = projectService.updateProject(id, projectDto);
        logger.debug("Updated project: {}.", result);
        return result;
    }

    @Operation(summary = "Archive a project")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject(@PathVariable Long id) {
        logger.debug("Deleting project with id: {}.", id);
        projectService.deleteProject(id);
        logger.debug("Successfully deleted project.");
    }

}
