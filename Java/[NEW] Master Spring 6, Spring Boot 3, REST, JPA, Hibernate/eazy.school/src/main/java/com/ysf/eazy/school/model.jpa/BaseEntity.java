package com.ysf.eazy.school.model.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @JsonIgnore // Ignore field in REST API JSON response
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @JsonIgnore // Ignore field in REST API response
    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @JsonIgnore // Ignore field in REST API response
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime updatedAt;

    @JsonIgnore // Ignore field in REST API response
    @LastModifiedBy
    @Column(insertable = false)
    private String updatedBy;
}
