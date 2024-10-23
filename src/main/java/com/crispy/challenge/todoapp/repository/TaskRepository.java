package com.crispy.challenge.todoapp.repository;

import com.crispy.challenge.todoapp.dto.DailyTaskDto;
import com.crispy.challenge.todoapp.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("""
            SELECT t.id as id, t.name as name, COUNT(s) as subtaskCount
            FROM Owner o
            JOIN o.projects p
            JOIN p.tasks t
            LEFT JOIN t.subtasks s
            WHERE o.id = :ownerId
            AND t.dueDate BETWEEN :startDate AND :endDate
            AND t.deleted = false
            AND (s.deleted = false OR t.subtasks IS EMPTY)
            GROUP BY t
            """)
    List<DailyTaskDto> findDailyTasks(Long ownerId, Instant startDate, Instant endDate);

    @Query("""
            SELECT t
            FROM Task t
            JOIN t.project p
            LEFT JOIN FETCH t.subtasks s
            WHERE t.id = :id
            AND p.owner.id = :ownerId
            AND t.deleted = false
            AND (s.deleted = false OR t.subtasks IS EMPTY)
            """)
    Optional<Task> findTaskWithSubtasks(Long id, Long ownerId);

    @Query("""
            SELECT t
            FROM Task t
            JOIN FETCH t.project p
            JOIN FETCH p.owner o
            WHERE t.id = :id
            AND o.id = :ownerId
            AND t.deleted = false
            """)
    Optional<Task> findByIdAndOwner(Long id, Long ownerId);

    @Query("""
            UPDATE Task t
            SET t.deleted = true
            WHERE t.id = :id
            AND t.project.owner.id = :ownerId
            """)
    @Modifying
    void softDeleteById(Long id, Long ownerId);

    @Modifying
    @Query("""
            UPDATE Task t
            SET t.deleted = true
            WHERE t.project.id = :projectId
            """)
    void softDeleteTasksByProjectId(Long projectId);

}
