package com.example.jangboo.role.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private RoleType role;

    @Column(name = "student_id")
    private Long studentId;

    @CreationTimestamp
    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Builder
    public Role(RoleType role, Long studentId) {
        this.role = role;
        this.studentId = studentId;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
