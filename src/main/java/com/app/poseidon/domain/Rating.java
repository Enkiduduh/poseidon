package com.app.poseidon.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.Digits;
import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Data
@Table(name = "rating")
public class Rating {
    // TODO: Map columns in data table RATING with corresponding java fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @NotBlank(message = "Rating is mandatory.")
    @Column(name = "moddys_rating", nullable = false)
    private String moodysRating;

    @NotBlank(message = "Rating is mandatory.")
    @Column(name = "sandprating", nullable = false)
    private String sandPRating;

    @NotBlank(message = "Rating is mandatory.")
    @Column(name = "fitch_rating", nullable = false)
    private String fitchRating;

    @Digits(integer = 4, fraction = 0)
    @Column(name = "order_number", nullable = false)
    private Integer orderNumber;
}
