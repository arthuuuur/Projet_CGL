package com.example.cglprojectv2.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Nom d'affaire obligatoire")
    @Size(max = 50, message = "Le nom d'affaire doit contenir maximum 100 caractères")
    private String name;

    @Column(nullable = false)
    @NotNull(message = "Montant de l'affaire obligatoire")
    @Positive(message = "Le montant de l'affaire doit être positif")
    private Double amount;

    @Column(nullable = false)
    @NotNull(message = "La date de l'affaire est obligatoire")
    private Date date;

    @ManyToOne
    @JoinColumn(nullable = false)
    private BusinessProvider businessProvider;

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL)
    private Set<Commission> commissions = new HashSet<>();

    /**
     * Add a commission to the business
     *
     * @param commission the commission to add
     */
    public void addCommission(Commission commission) {
        if (commissions == null) {
            commissions = new HashSet<>();
        }
        commissions.add(commission);
        commission.setBusiness(this);
    }

    /**
     * Get the full name of the business provider
     *
     * @return the full name of the business provider
     */
    public String getBusinessProviderFullName() {
        return businessProvider.getFullName();
    }

    /**
     * Get commission by depth from the original provider of the business
     * If the depth is 0, the commission is the one of the original provider
     * If the depth is 1, the commission is the one of the sponsor of the original provider
     * If the depth is 2, the commission is the one of the sponsor of the sponsor of the original provider
     *
     * @param depth the depth of the commission
     * @return the commission
     */
    public Commission getCommissionByDepthFromOriginalProvider(int depth) {
        return commissions.stream().filter(commission -> commission.getDepthFromOriginalProvider() == depth).findFirst().orElse(null);
    }
}
