package com.example.cglprojectv2.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BusinessProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Prénom obligatoire")
    @Size(max = 50, message = "Le prénom doit contenir maximum 50 caractères")
    @Pattern(regexp = "^[a-zA-Z ,.'-]+$", message = "Le prénom ne doit contenir que des lettres")
    private String firstName;

    @Column(nullable = false)
    @NotBlank(message = "Nom obligatoire")
    @Size(max = 50, message = "Le nom doit contenir maximum 50 caractères")
    @Pattern(regexp = "^[a-zA-Z ,.'-]+$", message = "Le nom ne doit contenir que des lettres")
    private String lastName;

    @Column(nullable = false)
    @NotBlank(message = "Username obligatoire")
    @Size(max = 50, message = "Le username doit contenir maximum 50 caractères")
    private String userName;

    @OneToMany(mappedBy = "businessProvider", cascade = CascadeType.REMOVE)
    private Set<Business> businesses = new HashSet<>();

    @OneToMany(mappedBy = "commissionOwner", cascade = CascadeType.REMOVE)
    private Set<Commission> commissions = new HashSet<>();

    @ManyToOne
    private BusinessProvider sponsor;

    @OneToMany(mappedBy = "sponsor", fetch = FetchType.EAGER)
    private Set<BusinessProvider> sponsored = new HashSet<>();

    public String getFirstName() {
        if (firstName == null) {
            return null;
        }
        return firstName.substring(0, 1).toUpperCase() + firstName.substring(1);
    }

    public String getLastName() {
        if (lastName == null) {
            return null;
        }
        return lastName.substring(0, 1).toUpperCase() + lastName.substring(1);
    }

    @JsonIgnore
    public Set<Business> getBusinesses() {
        return businesses;
    }

    @JsonIgnore
    public Set<BusinessProvider> getSponsored() {
        return sponsored;
    }

    public void setSponsor(@Nullable BusinessProvider businessProvider) {
        sponsor = businessProvider;
        if (businessProvider != null) {
            if (businessProvider.getSponsored() == null) {
                businessProvider.setSponsored(new HashSet<>());
            }
            businessProvider.getSponsored().add(this);
        }
    }

    /**
     * Add a business to the business provider
     *
     * @param business the business to add
     */
    public void addBusiness(Business business) {
        if (businesses == null) {
            businesses = new HashSet<>();
        }
        businesses.add(business);
        business.setBusinessProvider(this);
    }

    /**
     * Count the number of sponsor above the current business provider
     *
     * @return the number of sponsor
     */
    public Integer countSuccessiveSponsor() {
        return sponsor != null ? 1 + sponsor.countSuccessiveSponsor() : 0;
    }

    /**
     * Get the business provider full name
     *
     * @return the full name
     */
    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }
}