package com.crispy.challenge.todoapp.service;

import com.crispy.challenge.todoapp.converter.ProjectConverter;
import com.crispy.challenge.todoapp.converter.StatusConverter;
import com.crispy.challenge.todoapp.dto.ProjectDetailsDto;
import com.crispy.challenge.todoapp.dto.ProjectDto;
import com.crispy.challenge.todoapp.exception.NoResultFoundException;
import com.crispy.challenge.todoapp.model.Owner;
import com.crispy.challenge.todoapp.model.Project;
import com.crispy.challenge.todoapp.model.Status;
import com.crispy.challenge.todoapp.repository.OwnerRepository;
import com.crispy.challenge.todoapp.repository.ProjectRepository;
import com.crispy.challenge.todoapp.repository.TaskRepository;
import com.crispy.challenge.todoapp.security.CustomUserDetails;
import com.crispy.challenge.todoapp.security.OwnerUtils;
import com.crispy.challenge.todoapp.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectConverter projectConverter;

    @Mock
    private StatusConverter statusConverter;

    @InjectMocks
    private ProjectServiceImpl projectService;

    private ProjectDto projectDto;
    private ProjectDetailsDto projectDetailsDto;
    private Owner owner;
    private Project project;
    private Long ownerId;
    private Long projectId;

    @BeforeEach
    public void setUp() {
        CustomUserDetails userDetails = new CustomUserDetails(1L, "user", "password",
                Set.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.setContext(
                SecurityContextHolder.createEmptyContext()
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ownerId = OwnerUtils.getOwnerId();
        projectId = 1L;

        projectDto = new ProjectDto(1L, "project", Status.TO_DO.getValue());
        projectDetailsDto = new ProjectDetailsDto(projectDto, List.of());

        owner = new Owner();
        owner.setId(1L);
        owner.setUsername("user");

        project = new Project();
        project.setId(1L);
        project.setName("project");
        project.setStatus(Status.TO_DO);
    }

    @Test
    void testCreateProject() {
        when(ownerRepository.getReferenceById(ownerId)).thenReturn(owner);
        when(projectConverter.toProject(projectDto, owner)).thenReturn(project);
        when(projectRepository.save(project)).thenReturn(project);
        when(projectConverter.toProjectDto(project)).thenReturn(projectDto);

        ProjectDto result = projectService.createProject(projectDto);

        verify(projectRepository).save(project);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertEquals(projectDto, result)
        );
    }

    @Test
    void testGetProjectsByOwner() {
        when(projectRepository.findByOwnerId(ownerId)).thenReturn(List.of(project));
        when(projectConverter.toProjectDtoList(List.of(project))).thenReturn(List.of(projectDto));

        List<ProjectDto> result = projectService.getProjectsByOwner();

        verify(projectRepository).findByOwnerId(ownerId);
        verify(projectConverter).toProjectDtoList(List.of(project));

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).contains(projectDto)
        );
    }

    @Test
    void testGetArchivedProjectsByOwner() {
        when(projectRepository.findArchivedByOwnerId(ownerId)).thenReturn(List.of(project));
        when(projectConverter.toProjectDtoList(List.of(project))).thenReturn(List.of(projectDto));

        List<ProjectDto> result = projectService.getArchivedProjectsByOwner();

        verify(projectRepository).findArchivedByOwnerId(ownerId);
        verify(projectConverter).toProjectDtoList(List.of(project));

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).contains(projectDto)
        );
    }

    @Test
    void testGetProjectDetails_Success() {
        when(projectRepository.findProjectWithTasks(projectId, ownerId)).thenReturn(Optional.of(project));
        when(projectConverter.toProjectDetailsDto(project)).thenReturn(projectDetailsDto);

        ProjectDetailsDto result = projectService.getProjectDetails(projectId);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertEquals(projectDetailsDto, result)
        );

        verify(projectRepository).findProjectWithTasks(projectId, ownerId);
        verify(projectConverter).toProjectDetailsDto(project);
    }

    @Test
    void testGetProjectDetails_NotFound() {
        when(projectRepository.findProjectWithTasks(projectId, ownerId)).thenReturn(Optional.empty());

        assertThrows(NoResultFoundException.class,
                () -> projectService.getProjectDetails(projectId)
        );

        verify(projectRepository).findProjectWithTasks(projectId, ownerId);
        verify(projectConverter, never()).toProjectDetailsDto(any());
    }

    @Test
    void testUpdateProject_Success() {
        when(projectRepository.findByIdAndOwner(projectId, ownerId)).thenReturn(Optional.of(project));
        when(statusConverter.convert(any())).thenReturn(Status.fromValue(projectDto.status()));
        when(projectConverter.toProjectDto(project)).thenReturn(projectDto);

        when(projectRepository.save(project)).thenReturn(project);

        ProjectDto result = projectService.updateProject(projectId, projectDto);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertEquals(projectDto, result)
        );

        verify(projectRepository).findByIdAndOwner(projectId, ownerId);
        verify(projectConverter).toProjectDto(project);
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    void testUpdateProject_NotFound() {
        when(projectRepository.findByIdAndOwner(projectId, ownerId)).thenReturn(Optional.empty());

        assertThrows(NoResultFoundException.class,
                () -> projectService.updateProject(projectId, projectDto)
        );

        verify(projectRepository).findByIdAndOwner(projectId, ownerId);
        verify(projectRepository, never()).save(any());
        verify(projectConverter, never()).toProjectDto(any());
    }

    @Test
    void testDeleteProject() {
        projectService.deleteProject(projectId);

        verify(projectRepository).softDeleteById(projectId, ownerId);
    }
}
