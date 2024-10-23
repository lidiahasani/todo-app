package com.crispy.challenge.todoapp.service.impl;

import com.crispy.challenge.todoapp.converter.StatusConverter;
import com.crispy.challenge.todoapp.converter.SubtaskConverter;
import com.crispy.challenge.todoapp.dto.SubtaskDto;
import com.crispy.challenge.todoapp.exception.NoResultFoundException;
import com.crispy.challenge.todoapp.model.Subtask;
import com.crispy.challenge.todoapp.model.Task;
import com.crispy.challenge.todoapp.repository.SubtaskRepository;
import com.crispy.challenge.todoapp.repository.TaskRepository;
import com.crispy.challenge.todoapp.service.SubtaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.UnaryOperator;

import static com.crispy.challenge.todoapp.security.OwnerUtils.getOwnerId;

@Service
public class SubtaskServiceImpl implements SubtaskService {

    private final SubtaskRepository subtaskRepository;
    private final TaskRepository taskRepository;
    private final SubtaskConverter subtaskConverter;
    private final StatusConverter statusConverter;

    public SubtaskServiceImpl(SubtaskRepository subtaskRepository, TaskRepository taskRepository, SubtaskConverter subtaskConverter, StatusConverter statusConverter) {
        this.subtaskRepository = subtaskRepository;
        this.taskRepository = taskRepository;
        this.subtaskConverter = subtaskConverter;
        this.statusConverter = statusConverter;
    }

    @Override
    public SubtaskDto createSubtask(SubtaskDto subtaskDto, Long taskId) {
        Subtask subtask = taskRepository.findByIdAndOwner(taskId, getOwnerId())
                .map(task -> subtaskConverter.toSubtask(subtaskDto, task))
                .orElseThrow(() -> new NoResultFoundException("Task not found"));
        subtask = subtaskRepository.save(subtask);
        return subtaskConverter.toSubtaskDto(subtask);
    }

    @Override
    public SubtaskDto getSubtaskDetails(Long id) {
        return subtaskRepository.findByIdAndOwner(id, getOwnerId())
                .map(subtaskConverter::toSubtaskDto)
                .orElseThrow(() -> new NoResultFoundException("Subtask not found."));
    }

    @Override
    public SubtaskDto updateSubtask(Long id, SubtaskDto subtaskDto) {
        return subtaskRepository.findByIdAndOwner(id, getOwnerId())
                .map(updateSubtask(subtaskDto))
                .map(subtaskRepository::save)
                .map(subtaskConverter::toSubtaskDto)
                .orElseThrow(() -> new NoResultFoundException("Subtask not found."));
    }

    private UnaryOperator<Subtask> updateSubtask(SubtaskDto subtaskDto) {
        return subtask -> {
            subtask.setName(subtaskDto.name());
            subtask.setDescription(subtaskDto.description());
            subtask.setStatus(statusConverter.convert(subtaskDto.status()));
            Task task = taskRepository.findByIdAndOwner(subtaskDto.taskId(), getOwnerId())
                            .orElseThrow(() -> new NoResultFoundException("Task not found."));
            subtask.setTask(task);
            return subtask;
        };
    }

    @Override
    @Transactional
    public void deleteSubtask(Long id) {
        subtaskRepository.softDeleteById(id, getOwnerId());
    }
}
