package com.example.cglprojectv2.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Date;
import java.util.Set;

class BusinessTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private Business business;

    @BeforeEach
    void init() {
        BusinessProvider businessProvider = BusinessProvider.builder().firstName("Jhon").lastName("Snow").build();
        Commission commission = Commission.builder().build();

        business = Business.builder()
                .name("business")
                .amount(1000.0)
                .businessProvider(businessProvider)
                .commissions(Set.of(commission))
                .date(new Date())
                .build();
    }

    @Test
    void whenInstantiatingBusiness_givenIncorrectName_thenThrowException() {
        business.setName(null);
        Set<ConstraintViolation<Business>> violations = validator.validate(business);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("Nom d'affaire obligatoire", violations.iterator().next().getMessage());

        business.setName("");
        violations = validator.validate(business);
        Assertions.assertEquals(1, violations.size());

        business.setName("a".repeat(101));
        violations = validator.validate(business);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("Le nom d'affaire doit contenir maximum 100 caractères", violations.iterator().next().getMessage());
    }

    @Test
    void whenInstantiatingBusiness_givenIncorrectAmount_thenThrowException() {
        business.setAmount(null);
        Set<ConstraintViolation<Business>> violations = validator.validate(business);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("Montant de l'affaire obligatoire", violations.iterator().next().getMessage());

        business.setAmount(-1.0);
        violations = validator.validate(business);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("Le montant de l'affaire doit être positif", violations.iterator().next().getMessage());

        business.setAmount(0.0);
        violations = validator.validate(business);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("Le montant de l'affaire doit être positif", violations.iterator().next().getMessage());
    }

    @Test
    void whenInstantiatingBusiness_givenIncorrectDate_thenThrowException() {
        business.setDate(null);
        Set<ConstraintViolation<Business>> violations = validator.validate(business);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("La date de l'affaire est obligatoire", violations.iterator().next().getMessage());
    }

    @Test
    void whenAddingCommission_thenAlsoSetBusinessToCommission() {
        Commission commission = Commission.builder().business(business).build();

        business.setCommissions(null);

        business.addCommission(commission);
        Assertions.assertEquals(business, commission.getBusiness());
        Assertions.assertTrue(business.getCommissions().contains(commission));
    }

    @Test
    void get_commission_by_depth_should_return_the_commission_of_the_original_provider_if_depth_equal_0() {
        Business business = Business.builder().build();

        Commission commission1 = Commission.builder()
                .depthFromOriginalProvider(0)
                .build();

        Commission commission2 = Commission.builder()
                .depthFromOriginalProvider(1)
                .build();

        business.addCommission(commission1);
        business.addCommission(commission2);
        Assertions.assertEquals(commission1, business.getCommissionByDepthFromOriginalProvider(0));
    }

    @Test
    void getter_and_setter_should_work() {

        Assertions.assertEquals("business", business.getName());
        Assertions.assertEquals(1000.0, business.getAmount());
        Assertions.assertNotNull(business.getBusinessProvider());
        Assertions.assertNotNull(business.getCommissions());
        Assertions.assertNotNull(business.getDate());

        business.setName("business 2");
        business.setAmount(10000.0);
        business.setDate(new Date());

        Assertions.assertEquals("business 2", business.getName());
        Assertions.assertEquals(10000.0, business.getAmount());
        Assertions.assertNotNull(business.getDate());
    }

    @Test
    void whenInstanciatingBusiness_givenNoArgsConst() {
        Business business = new Business();
        Assertions.assertNull(business.getName());
        Assertions.assertNull(business.getAmount());
        Assertions.assertNull(business.getBusinessProvider());
        Assertions.assertTrue(business.getCommissions().isEmpty());
        Assertions.assertNull(business.getDate());
    }

    @Test
    void whenInstanciatingBusiness_thenIdShouldBeNull() {
        Assertions.assertNull(business.getId());
    }

    @Test
    void whenGetBusinessProviderFullName_thenReturnFullName() {
        Assertions.assertEquals("Jhon Snow", business.getBusinessProviderFullName());
    }

}