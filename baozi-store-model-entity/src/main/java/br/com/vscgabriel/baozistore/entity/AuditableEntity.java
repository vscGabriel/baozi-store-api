package br.com.vscgabriel.baozistore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public abstract class AuditableEntity {
    @Column(name = "creationdate", nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @Column(name = "updateddate", nullable = false)
    private LocalDateTime updatedDate;

    @Column(name = "creationuser", nullable = false, length = 100, updatable = false)
    private String creationUser;

    @Column(name = "lastupdateduser", nullable = false, length = 100)
    private String lastUpdatedUser;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        creationDate = now;
        updatedDate = now;
        creationUser = creationUser == null ? "SYSTEM" : creationUser;
        lastUpdatedUser = lastUpdatedUser == null ? creationUser : lastUpdatedUser;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = LocalDateTime.now();
        lastUpdatedUser = lastUpdatedUser == null ? creationUser : lastUpdatedUser;
    }

    public void deactivate() { active = false; }
}
