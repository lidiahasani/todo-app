package com.crispy.challenge.todoapp.service;

import com.crispy.challenge.todoapp.dto.SubtaskDto;

public interface SubtaskService {
    SubtaskDto createSubtask(SubtaskDto subtaskDto, Long taskId);

    SubtaskDto getSubtaskDetails(Long id);

    SubtaskDto updateSubtask(Long id, SubtaskDto subtaskDto);

    void deleteSubtask(Long id);
}
