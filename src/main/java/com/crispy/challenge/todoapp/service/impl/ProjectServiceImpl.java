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
import com.crispy.challenge.todoapp.repository.TaskRepository;
import com.crispy.challenge.todoapp.service.ProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.UnaryOperator;

import static com.crispy.challenge.todoapp.security.OwnerUtils.getOwnerId;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectConverter projectConverter;
    private final StatusConverter statusConverter;
    private final OwnerRepository ownerRepository;
    private final TaskRepository taskRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectConverter projectConverter, StatusConverter statusConverter, OwnerRepository ownerRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.projectConverter = projectConverter;
        this.statusConverter = statusConverter;
        this.ownerRepository = ownerRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public ProjectDto createProject(ProjectDto projectDto) {
        Owner owner = ownerRepository.getReferenceById(getOwnerId());
        Project project = projectConverter.toProject(projectDto, owner);
        project = projectRepository.save(project);
        return projectConverter.toProjectDto(project);
    }

    @Override
    public List<ProjectDto> getProjectsByOwner() {
        List<Project> projects = projectRepository.findByOwnerId(getOwnerId());
        return projectConverter.toProjectDtoList(projects);
    }

    @Override
    public List<ProjectDto> getArchivedProjectsByOwner() {
        List<Project> projects = projectRepository.findArchivedByOwnerId(getOwnerId());
        return projectConverter.toProjectDtoList(projects);
    }

    @Override
    public ProjectDetailsDto getProjectDetails(Long id) {
        return projectRepository.findProjectWithTasks(id, getOwnerId())
                .map(projectConverter::toProjectDetailsDto)
                .orElseThrow(() -> new NoResultFoundException("Project not found."));
    }

    @Override
    public ProjectDto updateProject(Long id, ProjectDto projectDto) {
        return projectRepository.findByIdAndOwner(id, getOwnerId())
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
        projectRepository.softDeleteById(id, getOwnerId());
        taskRepository.softDeleteTasksByProjectId(id);
    }
}
