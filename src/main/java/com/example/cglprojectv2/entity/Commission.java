package com.example.cglprojectv2.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Commission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull(message = "L'affaire est obligatoire")
    private Business business;

    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull(message = "Le receveur de la commission est obligatoire")
    private BusinessProvider commissionOwner;

    @Column(nullable = false)
    @NotNull(message = "Le montant de la commission est obligatoire")
    @Positive(message = "Le montant de la commission doit être positif")
    private Double amount;

    @Column(nullable = false)
    @NotNull(message = "Le pourcentage de la commission est obligatoire")
    @Positive(message = "Le pourcentage de la commission doit être positif")
    private Double percentageCommission;

    @Positive(message = "Le pourcentage de la commission du parrain doit être positif")
    private Double percentageCommissionSponsor;

    @Column(nullable = false)
    @NotNull(message = "La profondeur de la commission par rapport au fournisseur d'origine est obligatoire")
    //Level of the commission owner in the tree of sponsoring. 0 is the original provider, 1 is the first sponsor, 2 is the second sponsor, etc.
    private Integer depthFromOriginalProvider;

    @JsonIgnore
    public Business getBusiness() {
        return business;
    }

    @JsonIgnore
    public BusinessProvider getCommissionOwner() {
        return commissionOwner;
    }
}
