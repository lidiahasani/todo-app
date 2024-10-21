package com.crispy.challenge.todoapp.converter;

import com.crispy.challenge.todoapp.dto.ProjectDetailsDto;
import com.crispy.challenge.todoapp.dto.ProjectDto;
import com.crispy.challenge.todoapp.model.Owner;
import com.crispy.challenge.todoapp.model.Project;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectConverter {

    private final TaskConverter taskConverter;
    private final StatusConverter statusConverter;

    public ProjectConverter(TaskConverter taskConverter, StatusConverter statusConverter) {
        this.taskConverter = taskConverter;
        this.statusConverter = statusConverter;
    }

    public List<ProjectDto> toProjectDtoList(List<Project> projects) {
        return projects.stream()
                .map(this::toProjectDto)
                .toList();
    }

    public ProjectDto toProjectDto(Project project) {
        return new ProjectDto(project.getId(),
                project.getName(),
                project.getStatus().getValue());
    }

    public ProjectDetailsDto toProjectDetailsDto(Project project) {
        return new ProjectDetailsDto(toProjectDto(project),
                taskConverter.toTaskDtoList(project.getTasks()));
    }

    public Project toProject(ProjectDto projectDto, Owner owner) {
        Project project = new Project();
        project.setName(projectDto.name());
        project.setStatus(statusConverter.convert(projectDto.status()));
        project.setOwner(owner);
        return project;
    }

}
