package com.app.poseidon.domain;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "curvepoint")
public class CurvePoint {
    // TODO: Map columns in data table CURVEPOINT with corresponding java fields

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "curve_id")
    private Integer id;

    @NotNull
    @Column(name = "as_of_date", nullable = false, updatable = false)
    private LocalDateTime asOfDate;

    @Digits(integer = 6, fraction = 4)
    @Column(nullable = false)
    private BigDecimal term;

    @Digits(integer = 6, fraction = 4)
    @Column(nullable = false)
    private BigDecimal value;

    @NotNull
    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @PrePersist
    protected void onCreate() {
        this.asOfDate = LocalDateTime.now();
        this.creationDate = LocalDateTime.now();
    }
}
