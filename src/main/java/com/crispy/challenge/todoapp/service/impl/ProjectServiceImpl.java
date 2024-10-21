package com.crispy.challenge.todoapp.service.impl;

import com.crispy.challenge.todoapp.converter.ProjectConverter;
import com.crispy.challenge.todoapp.converter.StatusConverter;
import com.crispy.challenge.todoapp.dto.ProjectDetailsDto;
import com.crispy.challenge.todoapp.dto.ProjectDto;
import com.crispy.challenge.todoapp.exception.NoResultFoundException;
import com.crispy.challenge.todoapp.model.Owner;
import com.crispy.challenge.todoapp.model.Project;
import com.crispy.challenge.todoapp.repository.OwnerRepository;
import com.crispy.challenge.todoapp.repository.ProjectRepository;
import com.crispy.challenge.todoapp.service.ProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.UnaryOperator;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectConverter projectConverter;
    private final StatusConverter statusConverter;
    private final OwnerRepository ownerRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectConverter projectConverter, StatusConverter statusConverter, OwnerRepository ownerRepository) {
        this.projectRepository = projectRepository;
        this.projectConverter = projectConverter;
        this.statusConverter = statusConverter;
        this.ownerRepository = ownerRepository;
    }

    @Override
    public ProjectDto createProject(ProjectDto projectDto, Long ownerId) {
        Owner owner = ownerRepository.getReferenceById(ownerId);
        Project project = projectConverter.toProject(projectDto, owner);
        project = projectRepository.save(project);
        return projectConverter.toProjectDto(project);
    }

    @Override
    public List<ProjectDto> getProjectsByOwner(Long ownerId) {
        List<Project> projects = projectRepository.findByOwnerId(ownerId);
        return projectConverter.toProjectDtoList(projects);
    }

    @Override
    public List<ProjectDto> getArchivedProjectsByOwner(Long ownerId) {
        List<Project> projects = projectRepository.findArchivedByOwnerId(ownerId);
        return projectConverter.toProjectDtoList(projects);
    }

    @Override
    public ProjectDetailsDto getProjectDetails(Long id) {
        return projectRepository.findProjectWithTasks(id)
                .map(projectConverter::toProjectDetailsDto)
                .orElseThrow(() -> new NoResultFoundException("Project not found."));
    }

    @Override
    public ProjectDto updateProject(Long id, ProjectDto projectDto) {
        return projectRepository.findById(id)
                .map(updateProject(projectDto))
                .map(projectRepository::save)
                .map(projectConverter::toProjectDto)
                .orElseThrow(() -> new NoResultFoundException("Project not found."));
    }

    private UnaryOperator<Project> updateProject(ProjectDto projectDto) {
        return project -> {
            project.setName(projectDto.name());
            project.setStatus(statusConverter.convert(projectDto.status()));
            return project;
        };
    }

    @Override
    @Transactional
    public void deleteProject(Long id) {
        projectRepository.softDeleteById(id);
    }
}
