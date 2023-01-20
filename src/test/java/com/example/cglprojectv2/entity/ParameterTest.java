package com.example.cglprojectv2.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;

class ParameterTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private Parameters parameters;

    @BeforeEach
    void init() {
        parameters = Parameters.builder()
                .affiliatedDay(1)
                .minimumBusinessToBeAffiliated(1)
                .maxSuccessiveSponsor(1)
                .percentageCommission(0.05)
                .percentageCommissionSponsor(List.of(0.05))
                .build();
    }

    @Test
    void whenInstantiatingParameters_givenIncorrectAffiliatedDay_thenThrowException() {
        parameters.setAffiliatedDay(null);
        Assertions.assertEquals(1, validator.validate(parameters).size());

        parameters.setAffiliatedDay(0);
        Assertions.assertEquals(1, validator.validate(parameters).size());

        parameters.setAffiliatedDay(-1);
        Assertions.assertEquals(1, validator.validate(parameters).size());
    }

    @Test
    void whenInstantiatingParameters_givenIncorrectMinimumBusinessToBeAffiliated_thenThrowException() {
        parameters.setMinimumBusinessToBeAffiliated(null);
        Assertions.assertEquals(1, validator.validate(parameters).size());

        parameters.setMinimumBusinessToBeAffiliated(0);
        Assertions.assertEquals(1, validator.validate(parameters).size());

        parameters.setMinimumBusinessToBeAffiliated(-1);
        Assertions.assertEquals(1, validator.validate(parameters).size());
    }

    @Test
    void whenInstantiatingParameters_givenIncorrectMaxSuccessiveSponsor_thenThrowException() {
        parameters.setMaxSuccessiveSponsor(null);
        Assertions.assertEquals(1, validator.validate(parameters).size());

        parameters.setMaxSuccessiveSponsor(0);
        Assertions.assertEquals(1, validator.validate(parameters).size());

        parameters.setMaxSuccessiveSponsor(-1);
        Assertions.assertEquals(1, validator.validate(parameters).size());
    }

    @Test
    void whenInstantiatingParameters_givenIncorrectPercentageCommission_thenThrowException() {
        parameters.setPercentageCommission(null);
        Assertions.assertEquals(1, validator.validate(parameters).size());

        parameters.setPercentageCommission(0.0);
        Assertions.assertEquals(1, validator.validate(parameters).size());

        parameters.setPercentageCommission(-1.0);
        Assertions.assertEquals(1, validator.validate(parameters).size());
    }

    @Test
    void getter_and_setter_should_work() {
        Assertions.assertEquals(1, parameters.getAffiliatedDay());
        Assertions.assertEquals(1, parameters.getMinimumBusinessToBeAffiliated());
        Assertions.assertEquals(1, parameters.getMaxSuccessiveSponsor());
        Assertions.assertEquals(0.05, parameters.getPercentageCommission());
        Assertions.assertEquals(List.of((0.05)), parameters.getPercentageCommissionSponsor());

        parameters.setAffiliatedDay(2);
        parameters.setMinimumBusinessToBeAffiliated(2);
        parameters.setMaxSuccessiveSponsor(2);
        parameters.setPercentageCommission(0.06);
        parameters.setPercentageCommissionSponsor(List.of(0.01, 0.02));

        Assertions.assertEquals(2, parameters.getAffiliatedDay());
        Assertions.assertEquals(2, parameters.getMinimumBusinessToBeAffiliated());
        Assertions.assertEquals(2, parameters.getMaxSuccessiveSponsor());
        Assertions.assertEquals(0.06, parameters.getPercentageCommission());
        Assertions.assertEquals(List.of(0.01, 0.02), parameters.getPercentageCommissionSponsor());
    }

    @Test
    void whenInstantiatingParameters_givenNoArgsConst() {
        Parameters parameters = new Parameters();
        Assertions.assertNull(parameters.getAffiliatedDay());
        Assertions.assertNull(parameters.getMinimumBusinessToBeAffiliated());
        Assertions.assertNull(parameters.getMaxSuccessiveSponsor());
        Assertions.assertNull(parameters.getPercentageCommission());
        Assertions.assertTrue(parameters.getPercentageCommissionSponsor().isEmpty());
    }

    @Test
    void whenInstantiatingParameters_thenIdShouldBeNull() {
        Assertions.assertNull(parameters.getId());
    }
}
