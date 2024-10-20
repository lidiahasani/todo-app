package com.crispy.challenge.todoapp.repository;

import com.crispy.challenge.todoapp.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
}
