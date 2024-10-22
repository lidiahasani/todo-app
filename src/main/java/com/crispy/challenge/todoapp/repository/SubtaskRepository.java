package com.crispy.challenge.todoapp.repository;

import com.crispy.challenge.todoapp.model.Subtask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SubtaskRepository extends JpaRepository<Subtask, Long> {

    @Query(value = """
            UPDATE subtask SET deleted = true
            WHERE id = :id
            """, nativeQuery = true)
    @Modifying
    void softDeleteById(Long id);

}
