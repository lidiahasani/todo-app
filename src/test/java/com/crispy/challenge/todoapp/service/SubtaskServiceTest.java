package com.crispy.challenge.todoapp.service;

import com.crispy.challenge.todoapp.converter.StatusConverter;
import com.crispy.challenge.todoapp.converter.SubtaskConverter;
import com.crispy.challenge.todoapp.dto.SubtaskDto;
import com.crispy.challenge.todoapp.exception.NoResultFoundException;
import com.crispy.challenge.todoapp.model.Status;
import com.crispy.challenge.todoapp.model.Subtask;
import com.crispy.challenge.todoapp.model.Task;
import com.crispy.challenge.todoapp.repository.SubtaskRepository;
import com.crispy.challenge.todoapp.repository.TaskRepository;
import com.crispy.challenge.todoapp.security.CustomUserDetails;
import com.crispy.challenge.todoapp.security.OwnerUtils;
import com.crispy.challenge.todoapp.service.impl.SubtaskServiceImpl;
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

import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SubtaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private SubtaskRepository subtaskRepository;

    @Mock
    private SubtaskConverter subtaskConverter;

    @Mock
    private StatusConverter statusConverter;

    @InjectMocks
    private SubtaskServiceImpl subtaskService;

    private Long taskId;
    private Long subtaskId;
    private SubtaskDto subtaskDto;
    private Task task;
    private Subtask subtask;
    private Long ownerId;

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
        subtaskId = 1L;
        subtaskDto = new SubtaskDto(1L, "Subtask 1", "Description for Subtask 1",
                Status.TO_DO.getValue(), taskId);

        task = new Task();
        task.setId(taskId);

        subtask = new Subtask();
        subtask.setId(1L);
        subtask.setName("Subtask 1");
    }

    @Test
    void testCreateSubtask_Success() {
        when(taskRepository.findByIdAndOwner(taskId, ownerId)).thenReturn(Optional.of(task));
        when(subtaskConverter.toSubtask(subtaskDto, task)).thenReturn(subtask);
        when(subtaskRepository.save(subtask)).thenReturn(subtask);
        when(subtaskConverter.toSubtaskDto(subtask)).thenReturn(subtaskDto);

        SubtaskDto result = subtaskService.createSubtask(subtaskDto, taskId);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(subtaskDto, result)
        );

        verify(taskRepository).findByIdAndOwner(taskId, ownerId);
        verify(subtaskRepository).save(subtask);
    }

    @Test
    void testGetSubtaskDetails_Success() {
        when(subtaskRepository.findByIdAndOwner(subtaskId, ownerId)).thenReturn(Optional.of(subtask));
        when(subtaskConverter.toSubtaskDto(subtask)).thenReturn(subtaskDto);

        SubtaskDto result = subtaskService.getSubtaskDetails(subtaskId);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(subtaskDto, result)
        );

        verify(subtaskRepository).findByIdAndOwner(subtaskId, ownerId);
        verify(subtaskConverter).toSubtaskDto(subtask);
    }

    @Test
    void testGetSubtaskDetails_NotFound() {
        when(subtaskRepository.findByIdAndOwner(subtaskId, ownerId)).thenReturn(Optional.empty());

        assertThrows(NoResultFoundException.class,
                () -> subtaskService.getSubtaskDetails(taskId)
        );

        verify(subtaskRepository).findByIdAndOwner(subtaskId, ownerId);
        verify(subtaskConverter, never()).toSubtaskDto(any());
    }

    @Test
    void testUpdateSubtask_Success() {
        when(subtaskRepository.findByIdAndOwner(subtaskId, ownerId)).thenReturn(Optional.of(subtask));
        when(statusConverter.convert(subtaskDto.status())).thenReturn(subtask.getStatus());
        when(taskRepository.findByIdAndOwner(subtaskDto.taskId(), ownerId)).thenReturn(Optional.of(task));
        when(subtaskRepository.save(subtask)).thenReturn(subtask);
        when(subtaskConverter.toSubtaskDto(subtask)).thenReturn(subtaskDto);

        SubtaskDto result = subtaskService.updateSubtask(subtaskId, subtaskDto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(subtaskDto, result)
        );

        verify(subtaskRepository).findByIdAndOwner(subtaskId, ownerId);
        verify(subtaskRepository).save(subtask);
    }

    @Test
    void testUpdateSubtask_NotFound() {
        when(subtaskRepository.findByIdAndOwner(subtaskId, ownerId)).thenReturn(Optional.empty());

        assertThrows(NoResultFoundException.class,
                () -> subtaskService.updateSubtask(subtaskId, subtaskDto)
        );

        verify(taskRepository, never()).getReferenceById(any());
        verify(subtaskRepository, never()).save(any());
    }

    @Test
    void testDeleteSubtask_Success() {
        subtaskService.deleteSubtask(subtaskId);

        verify(subtaskRepository).softDeleteById(subtaskId, ownerId);
    }
}
