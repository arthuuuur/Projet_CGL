package com.example.cglprojectv2.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Date;
import java.util.Set;

class CommissionTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private Commission commission;
    private Business business;
    private BusinessProvider businessProvider;

    @BeforeEach
    void init() {
        businessProvider = BusinessProvider.builder()
                .firstName("Jhon")
                .lastName("Snow")
                .userName("winter_is_coming")
                .build();

        business = Business.builder()
                .name("business")
                .amount(1000.0)
                .businessProvider(businessProvider)
                .date(new Date())
                .build();

        commission = Commission.builder()
                .business(business)
                .commissionOwner(businessProvider)
                .amount(1000.0)
                .percentageCommission(0.05)
                .percentageCommissionSponsor(0.05)
                .depthFromOriginalProvider(0)
                .build();
    }

    @Test
    void whenInstantiatingCommission_givenIncorrectBusiness_thenThrowException() {
        commission.setBusiness(null);
        Set<ConstraintViolation<Commission>> violations = validator.validate(commission);
        System.out.println(violations);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("L'affaire est obligatoire", violations.iterator().next().getMessage());
    }

    @Test
    void whenInstantiatingCommission_givenIncorrectCommissionOwner_thenThrowException() {
        commission.setCommissionOwner(null);
        Set<ConstraintViolation<Commission>> violations = validator.validate(commission);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("Le receveur de la commission est obligatoire", violations.iterator().next().getMessage());
    }

    @Test
    void whenInstantiatingCommission_givenIncorrectAmount_thenThrowException() {
        commission.setAmount(null);
        Set<ConstraintViolation<Commission>> violations = validator.validate(commission);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("Le montant de la commission est obligatoire", violations.iterator().next().getMessage());

        commission.setAmount(-1.0);
        violations = validator.validate(commission);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("Le montant de la commission doit être positif", violations.iterator().next().getMessage());
    }

    @Test
    void whenInstantiatingCommission_givenIncorrectPercentageCommission_thenThrowException() {
        commission.setPercentageCommission(null);
        Set<ConstraintViolation<Commission>> violations = validator.validate(commission);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("Le pourcentage de la commission est obligatoire", violations.iterator().next().getMessage());

        commission.setPercentageCommission(-1.0);
        violations = validator.validate(commission);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("Le pourcentage de la commission doit être positif", violations.iterator().next().getMessage());
    }

    @Test
    void whenInstantiatingCommission_givenIncorrectPercentageCommissionSponsor_thenThrowException() {
        commission.setPercentageCommissionSponsor(-1.0);
        Set<ConstraintViolation<Commission>> violations = validator.validate(commission);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("Le pourcentage de la commission du parrain doit être positif", violations.iterator().next().getMessage());
    }

    @Test
    void whenInstantiatingCommission_givenIncorrectDepthFromOriginalProvider_thenThrowException() {
        commission.setDepthFromOriginalProvider(null);
        Set<ConstraintViolation<Commission>> violations = validator.validate(commission);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("La profondeur de la commission par rapport au fournisseur d'origine est obligatoire", violations.iterator().next().getMessage());
    }

    @Test
    void getter_and_setter_should_work() {
        Assertions.assertEquals(business, commission.getBusiness());
        Assertions.assertEquals(businessProvider, commission.getCommissionOwner());
        Assertions.assertEquals(1000.0, commission.getAmount());
        Assertions.assertEquals(0.05, commission.getPercentageCommission());
        Assertions.assertEquals(0.05, commission.getPercentageCommissionSponsor());
        Assertions.assertEquals(0, commission.getDepthFromOriginalProvider());


        commission.setAmount(10000.0);
        commission.setPercentageCommission(0.5);
        commission.setPercentageCommissionSponsor(0.5);
        commission.setDepthFromOriginalProvider(1);
        commission.setBusiness(business);
        commission.setCommissionOwner(businessProvider);
        Assertions.assertEquals(10000.0, commission.getAmount());
        Assertions.assertEquals(0.5, commission.getPercentageCommission());
        Assertions.assertEquals(0.5, commission.getPercentageCommissionSponsor());
        Assertions.assertEquals(1, commission.getDepthFromOriginalProvider());
        Assertions.assertEquals(business, commission.getBusiness());
        Assertions.assertEquals(businessProvider, commission.getCommissionOwner());
    }

    @Test
    void whenInstantiatingCommission_givenNoArgsConst_thenAllFieldsNull() {
        Commission commission = new Commission();
        Assertions.assertNull(commission.getBusiness());
        Assertions.assertNull(commission.getCommissionOwner());
        Assertions.assertNull(commission.getAmount());
        Assertions.assertNull(commission.getPercentageCommission());
        Assertions.assertNull(commission.getPercentageCommissionSponsor());
        Assertions.assertNull(commission.getDepthFromOriginalProvider());
    }

    @Test
    void whenInstantiatingCommision_thenIdShouldBeNull() {
        Assertions.assertNull(commission.getId());
    }
}