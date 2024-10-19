package com.crispy.challenge.todoapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@MappedSuperclass
public class BaseTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    private boolean deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public @NotNull Status getStatus() {
        return status;
    }

    public void setStatus(@NotNull Status status) {
        this.status = status;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
