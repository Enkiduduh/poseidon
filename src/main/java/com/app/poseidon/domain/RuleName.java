package com.app.poseidon.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "rulename")
public class RuleName {
    // TODO: Map columns in data table RULENAME with corresponding java fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank(message = "Name is mandatory.")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Description is mandatory.")
    @Column(nullable = false)
    private String description;

    @NotBlank(message = "Json is mandatory.")
    @Column(nullable = false)
    private String json;

    @Size(max = 512, message = "Template field must be under 512 characters")
    @NotBlank(message = "Template is mandatory.")
    @Column(nullable = false)
    private String template;

    @NotBlank(message = "SqlStr is mandatory.")
    @Column(name = "sql_str", nullable = false)
    private String sqlStr;

    @NotBlank(message = "SqlPart is mandatory.")
    @Column(name = "sql_part", nullable = false)
    private String sqlPart;
}
