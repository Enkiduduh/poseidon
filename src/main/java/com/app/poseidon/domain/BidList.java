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
    @Column(name = "bid_list_id")
    private Integer id;

    @Size(max = 30, message = "Account must be under 30 characters.")
    @NotNull
    @Column(nullable = false)
    private String account;

    @Size(max = 30, message = "Type must be under 30 characters.")
    @NotNull
    @Column(nullable = false)
    private String type;

    @Digits(integer = 6, fraction = 2, message = "La quantité d'enchère doit être un nombre valide avec jusqu'à 6 chiffres au total et 2 chiffres après la virgule.")
    @Column(name = "bid_quantity", nullable = false)
    private BigDecimal bidQuantity;

    // Champs optionnels avec valeurs par défaut
    @Digits(integer = 6, fraction = 2, message = "La quantité de demande doit être un nombre valide avec jusqu'à 6 chiffres au total et 2 chiffres après la virgule.")
    @Column(name = "ask_quantity")
    private BigDecimal askQuantity;

    @Digits(integer = 6, fraction = 2, message = "L'enchère doit être un nombre valide avec jusqu'à 6 chiffres au total et 2 chiffres après la virgule.")
    private BigDecimal bid;

    @Digits(integer = 6, fraction = 2, message = "La demande doit être un nombre valide avec jusqu'à 6 chiffres au total et 2 chiffres après la virgule.")
    private BigDecimal ask;

    private String benchmark;

    @Column(name = "bid_list_date", updatable = false)
    private LocalDateTime bidListDate;
    private String commentary;
    private String security;

    @Size(max = 10, message = "Status must be under 10 characters.")
    private String status;
    private String trader;
    private String book;

    @Column(name = "creation_name")
    private String creationName;

    @Column(name = "creation_date", updatable = false)
    private LocalDateTime creationDate;

    @Column(name = "revision_name")
    private String revisionName;

    @Column(name = "revision_date", updatable = false)
    private LocalDateTime revisionDate;

    @Column(name = "deal_name")
    private String dealName;

    @Column(name = "deal_type")
    private String dealType;

    @Column(name = "source_list_id")
    private String sourceListId;
    private String side;

    @PrePersist
    public void onCreate() {
        this.bidListDate = LocalDateTime.now();
        this.creationDate = LocalDateTime.now();
        this.revisionDate = LocalDateTime.now();
    }

    // Constructeur protégé
    protected BidList() {
    }

    public BidList(String account, String type, BigDecimal bidQuantity) {
        this.account = account;
        this.type = type;
        this.bidQuantity = bidQuantity;
    }
}
