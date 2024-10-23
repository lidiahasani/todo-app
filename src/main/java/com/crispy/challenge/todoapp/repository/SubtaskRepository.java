package com.crispy.challenge.todoapp.repository;

import com.crispy.challenge.todoapp.model.Subtask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SubtaskRepository extends JpaRepository<Subtask, Long> {

    @Query("""
            SELECT s
            FROM Subtask s
            JOIN FETCH s.task t
            JOIN FETCH t.project p
            WHERE s.id = :id
            AND p.owner.id = :ownerId
            AND s.deleted = false
            """)
    Optional<Subtask> findByIdAndOwner(Long id, Long ownerId);

    @Query("""
            UPDATE Subtask s
            SET s.deleted = true
            WHERE s.id = :id
            AND s.task.project.owner.id = :ownerId
            """)
    @Modifying
    void softDeleteById(Long id, Long ownerId);

    @Modifying
    @Query("""
            UPDATE Subtask s
            SET s.deleted = true
            WHERE s.task.id = :taskId
            """)
    void softDeleteSubtasksByTaskId(Long taskId);

}
