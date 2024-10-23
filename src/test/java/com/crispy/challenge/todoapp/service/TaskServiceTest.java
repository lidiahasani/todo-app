package com.crispy.challenge.todoapp.service;

import com.crispy.challenge.todoapp.converter.PriorityConverter;
import com.crispy.challenge.todoapp.converter.StatusConverter;
import com.crispy.challenge.todoapp.converter.TaskConverter;
import com.crispy.challenge.todoapp.dto.DailyTaskDto;
import com.crispy.challenge.todoapp.dto.TaskDetailsDto;
import com.crispy.challenge.todoapp.dto.TaskDto;
import com.crispy.challenge.todoapp.exception.NoResultFoundException;
import com.crispy.challenge.todoapp.model.Priority;
import com.crispy.challenge.todoapp.model.Project;
import com.crispy.challenge.todoapp.model.Status;
import com.crispy.challenge.todoapp.model.Task;
import com.crispy.challenge.todoapp.repository.ProjectRepository;
import com.crispy.challenge.todoapp.repository.SubtaskRepository;
import com.crispy.challenge.todoapp.repository.TaskRepository;
import com.crispy.challenge.todoapp.security.CustomUserDetails;
import com.crispy.challenge.todoapp.security.OwnerUtils;
import com.crispy.challenge.todoapp.service.impl.TaskServiceImpl;
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

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskConverter taskConverter;

    @Mock
    private StatusConverter statusConverter;

    @Mock
    SubtaskRepository subtaskRepository;

    @Mock
    private PriorityConverter priorityConverter;

    @InjectMocks
    private TaskServiceImpl taskService;

    private TaskDto taskDto;
    private Project project;
    private Task task;
    private LocalDate date;
    private DailyTaskDto dailyTaskDto1;
    private DailyTaskDto dailyTaskDto2;
    private Long ownerId;
    private Long taskId;

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
        taskId = 1L;

        date = LocalDate.now();
        Instant dueDate = Instant.now().plus(2, ChronoUnit.DAYS);
        taskDto = new TaskDto(1L, "Task 1", "Description of task 1",
                Status.TO_DO.getValue(), dueDate, Priority.MEDIUM.getValue(), 1L);

        project = new Project();
        project.setId(1L);

        task = new Task();
        task.setId(1L);
        task.setName("Task 1");
        task.setDescription("Description of task 1");
        task.setDueDate(dueDate);
    }

    @Test
    void testCreateTask() {
        Long projectId = 1L;

        when(projectRepository.findByIdAndOwner(projectId, ownerId)).thenReturn(Optional.of(project));
        when(taskConverter.toTask(taskDto, project)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskConverter.toTaskDto(task)).thenReturn(taskDto);

        TaskDto result = taskService.createTask(taskDto, projectId);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertEquals(taskDto, result)
        );

        verify(projectRepository).findByIdAndOwner(projectId, ownerId);
        verify(taskConverter).toTask(taskDto, project);
        verify(taskRepository).save(task);
        verify(taskConverter).toTaskDto(task);
    }

    @Test
    void testGetDailyTasks() {
        List<DailyTaskDto> expectedTasks = new ArrayList<>();
        expectedTasks.add(dailyTaskDto1);
        expectedTasks.add(dailyTaskDto2);

        when(taskRepository.findDailyTasks(any(), any(), any())).thenReturn(expectedTasks);

        List<DailyTaskDto> result = taskService.getDailyTasks(date);

        assertAll("Daily Tasks",
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result).containsExactlyInAnyOrder(dailyTaskDto1, dailyTaskDto2)
        );
    }

    @Test
    void testGetTaskDetails_Success() {
        TaskDetailsDto taskDetailsDto = new TaskDetailsDto(taskDto, List.of(), 2L);

        when(taskRepository.findTaskWithSubtasks(taskId, ownerId)).thenReturn(Optional.of(task));
        when(taskConverter.toTaskDetailsDto(task)).thenReturn(taskDetailsDto);

        TaskDetailsDto result = taskService.getTaskDetails(taskId);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertEquals(taskDetailsDto, result)
        );

        verify(taskRepository).findTaskWithSubtasks(taskId, ownerId);
        verify(taskConverter).toTaskDetailsDto(task);
    }

    @Test
    void testGetTaskDetails_NotFound() {
        when(taskRepository.findTaskWithSubtasks(taskId, ownerId)).thenReturn(Optional.empty());

        assertThrows(NoResultFoundException.class,
                () -> taskService.getTaskDetails(taskId)
        );

        verify(taskRepository).findTaskWithSubtasks(taskId, ownerId);
        verify(taskConverter, never()).toTaskDetailsDto(any());
    }

    @Test
    void testUpdateTask_Success() {
        when(taskRepository.findByIdAndOwner(taskId, ownerId)).thenReturn(Optional.of(task));
        when(projectRepository.findByIdAndOwner(taskDto.projectId(), ownerId)).thenReturn(Optional.of(project));
        when(statusConverter.convert(any())).thenReturn(Status.fromValue(taskDto.status()));
        when(priorityConverter.convert(any())).thenReturn(Priority.fromValue(taskDto.priority()));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskConverter.toTaskDto(task)).thenReturn(taskDto);

        TaskDto result = taskService.updateTask(taskId, taskDto);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertEquals(taskDto, result)
        );

        verify(taskRepository).findByIdAndOwner(taskId, ownerId);
        verify(taskRepository).save(task);
    }

    @Test
    void testUpdateTask_NotFound() {
        when(taskRepository.findByIdAndOwner(taskId, ownerId)).thenReturn(Optional.empty());

        assertThrows(NoResultFoundException.class,
                () -> taskService.updateTask(taskId, taskDto)
        );

        verify(taskRepository).findByIdAndOwner(taskId, ownerId);
        verify(taskConverter, never()).toTaskDto(any());
    }

    @Test
    void testDeleteTask() {
        taskService.deleteTask(taskId);

        verify(taskRepository).softDeleteById(taskId, ownerId);
    }
}
