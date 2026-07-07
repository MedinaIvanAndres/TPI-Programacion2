package src.entities;

import java.time.LocalDateTime;

public abstract class Base {
    private Long id;
    private LocalDateTime createdAt;

    public Base() {
        this(null, LocalDateTime.now());
    }

    public Base(Long id, LocalDateTime createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public abstract String toString();
}