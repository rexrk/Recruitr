package com.raman.recruitr.entity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JobAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Job job;

    @ManyToOne
    private Candidate candidate;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status; // APPLIED, SELECTED, REJECTED

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        if (status == null) status = ApplicationStatus.APPLIED;
    }

    public enum ApplicationStatus {
        APPLIED, SELECTED, REJECTED
    }
}
