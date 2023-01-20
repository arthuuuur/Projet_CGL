package com.example.cglprojectv2.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Date;
import java.util.Set;

class BusinessProviderTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private BusinessProvider businessProvider;

    @BeforeEach
    void init() {
        BusinessProvider sponsor = BusinessProvider.builder().firstName("sponsor").build();
        BusinessProvider sponsored = BusinessProvider.builder().firstName("sponsored").build();
        Business business = Business.builder().build();
        Commission commission = Commission.builder().build();

        businessProvider = BusinessProvider.builder()
                .firstName("Jhon")
                .lastName("Snow")
                .userName("winter_is_coming")
                .sponsor(sponsor)
                .sponsored(Set.of(sponsored))
                .businesses(Set.of(business))
                .commissions(Set.of(commission))
                .build();
    }

    @Test
    void whenInstantiatingBusinessProvider_givenIncorrectFirstName_thenThrowException() {
        businessProvider.setFirstName(null);
        Set<ConstraintViolation<BusinessProvider>> violations = validator.validate(businessProvider);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("Prénom obligatoire", violations.iterator().next().getMessage());

        businessProvider.setFirstName("");
        violations = validator.validate(businessProvider);
        Assertions.assertEquals(2, violations.size());

        businessProvider.setFirstName("Jhon1");
        violations = validator.validate(businessProvider);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("Le prénom ne doit contenir que des lettres", violations.iterator().next().getMessage());

        businessProvider.setFirstName("Jhon!");
        violations = validator.validate(businessProvider);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("Le prénom ne doit contenir que des lettres", violations.iterator().next().getMessage());

        businessProvider.setFirstName("a".repeat(51));
        violations = validator.validate(businessProvider);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("Le prénom doit contenir maximum 50 caractères", violations.iterator().next().getMessage());
    }

    @Test
    void whenInstantiatingBusinessProvider_givenIncorrectLastName_thenThrowException() {
        businessProvider.setLastName(null);
        Set<ConstraintViolation<BusinessProvider>> violations = validator.validate(businessProvider);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("Nom obligatoire", violations.iterator().next().getMessage());

        businessProvider.setLastName("");
        violations = validator.validate(businessProvider);
        Assertions.assertEquals(2, violations.size());

        businessProvider.setLastName("Snow1");
        violations = validator.validate(businessProvider);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("Le nom ne doit contenir que des lettres", violations.iterator().next().getMessage());

        businessProvider.setLastName("Snow!");
        violations = validator.validate(businessProvider);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("Le nom ne doit contenir que des lettres", violations.iterator().next().getMessage());

        businessProvider.setLastName("a".repeat(51));
        violations = validator.validate(businessProvider);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("Le nom doit contenir maximum 50 caractères", violations.iterator().next().getMessage());
    }

    @Test
    void whenInstanciatingBusinessProvider_givenIncorrectUserName_thenThrowException() {
        businessProvider.setUserName(null);
        Set<ConstraintViolation<BusinessProvider>> violations = validator.validate(businessProvider);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("Username obligatoire", violations.iterator().next().getMessage());

        businessProvider.setUserName("");
        violations = validator.validate(businessProvider);
        Assertions.assertEquals(1, violations.size());

        businessProvider.setUserName("a".repeat(51));
        violations = validator.validate(businessProvider);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("Le username doit contenir maximum 50 caractères", violations.iterator().next().getMessage());
    }

    @Test
    void whenInstanciatingBusinessProvider_givenNullSponsor_thenOk() {
        businessProvider.setSponsor(null);
        Set<ConstraintViolation<BusinessProvider>> violations = validator.validate(businessProvider);
        Assertions.assertEquals(0, violations.size());
    }

    @Test
    void whenInstanciatingBusinessProvider_givenNullSponsored_thenOk() {
        businessProvider.setSponsored(null);
        Set<ConstraintViolation<BusinessProvider>> violations = validator.validate(businessProvider);
        Assertions.assertEquals(0, violations.size());
    }

    @Test
    void whenInstanciatingBusinessProvider_givenNullBusinesses_thenOk() {
        businessProvider.setBusinesses(null);
        Set<ConstraintViolation<BusinessProvider>> violations = validator.validate(businessProvider);
        Assertions.assertEquals(0, violations.size());
    }

    @Test
    void whenInstanciatingBusinessProvider_givenNullCommissions_thenOk() {
        businessProvider.setCommissions(null);
        Set<ConstraintViolation<BusinessProvider>> violations = validator.validate(businessProvider);
        Assertions.assertEquals(0, violations.size());
    }

    @Test
    void whenAddingBusiness_thenAlsoSetBusinessProviderToBusiness() {
        Business business = Business.builder()
                .name("business")
                .amount(1000.0)
                .businessProvider(null)
                .date(new Date())
                .build();

        businessProvider.setBusinesses(null);

        businessProvider.addBusiness(business);

        Assertions.assertEquals(businessProvider, business.getBusinessProvider());
        Assertions.assertTrue(businessProvider.getBusinesses().contains(business));
    }

    @Test
    void countSuccessiveSponsor_should_return_0_when_no_sponsor() {
        BusinessProvider businessProvider = BusinessProvider.builder()
                .firstName("Jhon")
                .lastName("Snow")
                .userName("winter_is_coming")
                .build();

        Assertions.assertEquals(0, businessProvider.countSuccessiveSponsor());
    }

    @Test
    void countSuccessiveSponsor_should_return_1_when_one_sponsor() {
        BusinessProvider businessProvider = BusinessProvider.builder()
                .firstName("Jhon")
                .lastName("Snow")
                .userName("winter_is_coming")
                .sponsor(BusinessProvider.builder().build())
                .build();

        Assertions.assertEquals(1, businessProvider.countSuccessiveSponsor());
    }

    @Test
    void countSuccessiveSponsor_should_return_2_when_two_sponsor() {
        BusinessProvider businessProvider = BusinessProvider.builder()
                .firstName("Jhon")
                .lastName("Snow")
                .userName("winter_is_coming")
                .sponsor(BusinessProvider.builder()
                        .sponsor(BusinessProvider.builder().build())
                        .build())
                .build();

        Assertions.assertEquals(2, businessProvider.countSuccessiveSponsor());
    }

    @Test
    void getter_and_setter_should_work() {
        Assertions.assertEquals("Jhon", businessProvider.getFirstName());
        Assertions.assertEquals("Snow", businessProvider.getLastName());
        Assertions.assertEquals("winter_is_coming", businessProvider.getUserName());
        Assertions.assertEquals("Sponsor", businessProvider.getSponsor().getFirstName());
        Assertions.assertEquals("Sponsored", businessProvider.getSponsored().stream().findFirst().get().getFirstName());
        Assertions.assertEquals(1, businessProvider.getBusinesses().size());
        Assertions.assertEquals(1, businessProvider.getCommissions().size());

        businessProvider.setFirstName("Jhon");
        businessProvider.setLastName("Snow");
        businessProvider.setUserName("winter_is_coming");

        Assertions.assertEquals("Jhon", businessProvider.getFirstName());
        Assertions.assertEquals("Snow", businessProvider.getLastName());
        Assertions.assertEquals("winter_is_coming", businessProvider.getUserName());
    }

    @Test
    void whenInstanciatingBusinessProvider_givenNoArgsConst_thenAllFieldsNull() {
        BusinessProvider businessProvider = new BusinessProvider();
        System.out.println(businessProvider);
        Assertions.assertNull(businessProvider.getFirstName());
        Assertions.assertNull(businessProvider.getLastName());
        Assertions.assertNull(businessProvider.getUserName());
        Assertions.assertNull(businessProvider.getSponsor());
        Assertions.assertEquals(0, businessProvider.getSponsored().size());
        Assertions.assertEquals(0, businessProvider.getBusinesses().size());
        Assertions.assertEquals(0, businessProvider.getCommissions().size());
    }

    @Test
    void whenInstanciatingBusinessProvider_thenIdShouldBeNull() {
        Assertions.assertNull(businessProvider.getId());
    }

    @Test
    void whenGetFullName_thenOk() {
        Assertions.assertEquals("Jhon Snow", businessProvider.getFullName());
    }
}
