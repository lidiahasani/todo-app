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
        Task task = taskRepository.getReferenceById(taskId);
        Subtask subtask = subtaskConverter.toSubtask(subtaskDto, task);
        subtask = subtaskRepository.save(subtask);
        return subtaskConverter.toSubtaskDto(subtask);
    }

    @Override
    public SubtaskDto getSubtaskDetails(Long id) {
        return subtaskRepository.findById(id)
                .map(subtaskConverter::toSubtaskDto)
                .orElseThrow(() -> new NoResultFoundException("Subtask not found."));    }

    @Override
    public SubtaskDto updateSubtask(Long id, SubtaskDto subtaskDto) {
        return subtaskRepository.findById(id)
                .map(updateSubtask(subtaskDto))
                .map(subtaskRepository::save)
                .map(subtaskConverter::toSubtaskDto)
                .orElseThrow(() -> new NoResultFoundException("Task not found."));
    }

    private UnaryOperator<Subtask> updateSubtask(SubtaskDto subtaskDto) {
        return subtask -> {
            subtask.setName(subtaskDto.name());
            subtask.setDescription(subtaskDto.description());
            subtask.setStatus(statusConverter.convert(subtaskDto.status()));
            subtask.setTask(taskRepository.getReferenceById(subtaskDto.taskId()));
            return subtask;
        };
    }

    @Override
    @Transactional
    public void deleteSubtask(Long id) {
        subtaskRepository.softDeleteById(id);
    }
}
