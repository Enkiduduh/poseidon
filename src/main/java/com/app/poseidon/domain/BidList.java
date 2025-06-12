package com.app.poseidon.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "bidlist")
public class BidList {
    // TODO: Map columns in data table BIDLIST with corresponding java fields

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BidListId")
    private Integer id;

    @Size(max = 30, message = "Account must be under 30 characters.")
    @NotNull
    @Column(nullable = false)
    private String account;

    @Size(max = 30, message = "Type must be under 30 characters.")
    @NotNull
    @Column(nullable = false)
    private String type;

    @Digits(integer = 6, fraction = 4, message = "La quantité d'enchère doit être un nombre valide avec jusqu'à 6 chiffres au total et 2 chiffres après la virgule.")
    @Column(nullable = false)
    private BigDecimal bidQuantity;

    @Digits(integer = 6, fraction = 4, message = "La quantité de demande doit être un nombre valide avec jusqu'à 6 chiffres au total et 2 chiffres après la virgule.")
    @Column(nullable = false)
    private BigDecimal askQuantity;

    @Digits(integer = 6, fraction = 4, message = "L'enchère doit être un nombre valide avec jusqu'à 6 chiffres au total et 2 chiffres après la virgule.")
    @Column(nullable = false)
    private BigDecimal bid;

    @Digits(integer = 6, fraction = 4, message = "La demande doit être un nombre valide avec jusqu'à 6 chiffres au total et 2 chiffres après la virgule.")
    @Column(nullable = false)
    private BigDecimal  ask;

    private String benchmark;

    @Column(name = "bidListDate", nullable = false, updatable = false)
    private LocalDateTime bidListDate;

    @NotBlank(message = "Commentary is mandatory.")
    private String commentary;

    private String security;

    @Size(max = 10, message = "Status must be under 10 characters.")
    private String status;
    private String trader;
    private String book;

    @NotBlank(message = "Name is mandatory.")
    private String creationName;

    @Column(name = "creationDate", nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @NotBlank(message = "Revision name is mandatory.")
    private String revisionName;

    @Column(name = "revisionDate", nullable = false, updatable = false)
    private LocalDateTime revisionDate;

    @NotBlank(message = "Deal name is mandatory.")
    @Column(nullable = false)
    private String dealName;

    @Column(nullable = false)
    private String dealType;

    private String sourceListId;
    private String side;

    @PrePersist
    public void onCreate() {
        this.bidListDate = LocalDateTime.now();
        this.creationDate = LocalDateTime.now();
        this.revisionDate = LocalDateTime.now();
    }
}
