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
            WHERE p.owner.id = :ownerId
            AND p.deleted = false
            """)
    List<Project> findByOwnerId(Long ownerId);

    @Query("""
            SELECT p
            FROM Project p
            WHERE p.owner.id = :ownerId
            AND p.deleted = true
            """)
    List<Project> findArchivedByOwnerId(Long ownerId);

    @Query("""
            SELECT p
            FROM Project p
            LEFT JOIN FETCH p.tasks
            WHERE p.id = :id
            AND p.deleted = false
            """)
    Optional<Project> findProjectWithTasks(Long id);

    @Modifying
    @Query(value = """
            UPDATE project SET deleted = true
            WHERE id = :id
            """, nativeQuery = true)
    void softDeleteById(Long id);

}
