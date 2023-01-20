package com.example.cglprojectv2.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Parameters {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull(message = "Ce champs est obligatoire")
    @Positive(message = "Ce champs doit être positif")
    //The number of days during which the businessProvider is still considered affiliated even if he hasn't made any business
    private Integer affiliatedDay;

    @Column(nullable = false)
    @NotNull(message = "Ce champs est obligatoire")
    @Positive(message = "Ce champs doit être positif")
    //The number of business that a businessProvider must have made to be considered affiliated
    private Integer minimumBusinessToBeAffiliated;

    @Column(nullable = false)
    @NotNull(message = "Ce champs est obligatoire")
    @Positive(message = "Ce champs doit être positif")
    //The number of succesive sponsor allowed
    private Integer maxSuccessiveSponsor;

    @Column(nullable = false)
    @NotNull(message = "Ce champs est obligatoire")
    @Positive(message = "Ce champs doit être positif")
    //The percentage of the commission that the businessProvider will receive
    private Double percentageCommission;

    //We use a list to store the percentage of commission for each successive sponsor
    //The first element is the percentage for the sponsor of the businessProvider, the second element is the percentage for the sponsor of the sponsor of the originalProvider, etc...)
    @ElementCollection
    private List<Double> percentageCommissionSponsor = new ArrayList<>();

}
