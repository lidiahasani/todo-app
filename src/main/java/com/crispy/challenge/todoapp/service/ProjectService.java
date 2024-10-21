package com.crispy.challenge.todoapp.service;

import com.crispy.challenge.todoapp.dto.ProjectDetailsDto;
import com.crispy.challenge.todoapp.dto.ProjectDto;

import java.util.List;

public interface ProjectService {
    ProjectDto createProject(ProjectDto projectDto, Long ownerId);

    List<ProjectDto> getProjectsByOwner(Long ownerId);

    List<ProjectDto> getArchivedProjectsByOwner(Long ownerId);

    ProjectDetailsDto getProjectDetails(Long id);

    ProjectDto updateProject(Long id, ProjectDto projectDto);

    void deleteProject(Long id);
}
