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
            AND s.deleted = false
            GROUP BY t
            """)
    List<DailyTaskDto> findDailyTasks(Long ownerId, Instant startDate, Instant endDate);

    @Query("""
            SELECT t
            FROM Task t
            LEFT JOIN FETCH t.subtasks s
            WHERE t.id = :id
            AND t.deleted = false
            AND s.deleted = false
            """)
    Optional<Task> findTaskWithSubtasks(Long id);

    @Query(value = """
            UPDATE task SET deleted = true
            WHERE id = :id
            """, nativeQuery = true)
    @Modifying
    void softDeleteById(Long id);

}
