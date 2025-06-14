package com.app.poseidon.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Data
@Table(name = "trade")
public class Trade {
    // TODO: Map columns in data table TRADE with corresponding java fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trade_id")
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
    @Column(name = "buy_quantity", nullable = false)
    private BigDecimal buyQuantity;

    @Digits(integer = 6, fraction = 4, message = "La quantité de demande doit être un nombre valide avec jusqu'à 6 chiffres au total et 2 chiffres après la virgule.")
    @Column(name = "sell_quantity", nullable = false)
    private BigDecimal sellQuantity;

    @Digits(integer = 6, fraction = 4, message = "L'enchère doit être un nombre valide avec jusqu'à 6 chiffres au total et 2 chiffres après la virgule.")
    @Column(name ="buy_price", nullable = false)
    private BigDecimal buyPrice;

    @Digits(integer = 6, fraction = 4, message = "La demande doit être un nombre valide avec jusqu'à 6 chiffres au total et 2 chiffres après la virgule.")
    @Column(name ="sell_price", nullable = false)
    private BigDecimal  sellPrice;

    @Column(name = "trade_date", nullable = false, updatable = false)
    private LocalDateTime tradeDate;

    private String benchmark;

    @Column(name = "bid_list_date", nullable = false, updatable = false)
    private LocalDateTime bidListDate;

    @NotBlank(message = "Commentary is mandatory.")
    private String commentary;

    private String security;

    @Size(max = 10, message = "Status must be under 10 characters.")
    private String status;
    private String trader;
    private String book;

    @NotBlank(message = "Name is mandatory.")
    @Column(name = "creation_name")
    private String creationName;

    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @NotBlank(message = "Revision name is mandatory.")
    @Column(name = "revision_date")
    private String revisionName;

    @Column(name = "revision_date", nullable = false, updatable = false)
    private LocalDateTime revisionDate;

    @NotBlank(message = "Deal name is mandatory.")
    @Column(name = "deal_name", nullable = false)
    private String dealName;

    @Column(name = "deal_type", nullable = false)
    private String dealType;

    @Column(name = "source_list_id", nullable = false)
    private String sourceListId;

    private String side;

    @PrePersist
    protected void onCreate() {
        this.tradeDate = LocalDateTime.now();
        this.creationDate = LocalDateTime.now();
        this.revisionDate = LocalDateTime.now();
    }
}

