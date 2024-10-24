package com.crispy.challenge.todoapp.repository;

import com.crispy.challenge.todoapp.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("""
            SELECT p
            FROM Project p
            JOIN FETCH p.owner o
            WHERE o.id = :ownerId
            AND p.deleted = false
            """)
    List<Project> findByOwnerId(Long ownerId);

    @Query("""
            SELECT p
            FROM Project p
            JOIN FETCH p.owner o
            WHERE o.id = :ownerId
            AND p.deleted = true
            """)
    List<Project> findArchivedByOwnerId(Long ownerId);

    @Query("""
            SELECT p
            FROM Project p
            LEFT JOIN FETCH p.tasks t
            WHERE p.id = :id
            AND p.owner.id = :ownerId
            AND p.deleted = false
            AND (t.deleted = false OR p.tasks IS EMPTY)
            """)
    Optional<Project> findProjectWithTasks(Long id, Long ownerId);

    @Query("""
            SELECT p
            FROM Project p
            JOIN FETCH p.owner o
            WHERE p.id = :id
            AND o.id = :ownerId
            AND p.deleted = false
            """)
    Optional<Project> findByIdAndOwner(Long id, Long ownerId);

    @Modifying
    @Query("""
            UPDATE Project p SET p.deleted = true
            WHERE p.id = :id
            AND p.owner.id = :ownerId
            """)
    void softDeleteById(Long id, Long ownerId);

}
