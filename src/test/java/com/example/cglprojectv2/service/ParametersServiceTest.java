package com.example.cglprojectv2.service;

import com.example.cglprojectv2.entity.Parameters;
import com.example.cglprojectv2.repository.ParametersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ParametersServiceTest {

    private ParametersService service;
    private ParametersRepository repository;

    @BeforeEach
    void init(){
        repository = mock(ParametersRepository.class);
        service = new ParametersService(repository);
    }

    @Test
    void testGetParameters() {
        Parameters parameters = Parameters.builder()
                .affiliatedDay(30)
                .maxSuccessiveSponsor(5)
                .minimumBusinessToBeAffiliated(1)
                .percentageCommission(0.05)
                .percentageCommissionSponsor(List.of(0.05, 0.05, 0.05, 0.05, 0.05))
                .build();
        when(repository.findAll()).thenReturn(Collections.singletonList(parameters));
        assertEquals(parameters, service.getParameters());

        when(repository.findAll()).thenReturn(Collections.emptyList());
        Parameters result = service.getParameters();

        assertEquals(parameters.getAffiliatedDay(), result.getAffiliatedDay());
        assertEquals(parameters.getMaxSuccessiveSponsor(), result.getMaxSuccessiveSponsor());
        assertEquals(parameters.getMinimumBusinessToBeAffiliated(), result.getMinimumBusinessToBeAffiliated());
        assertEquals(parameters.getPercentageCommission(), result.getPercentageCommission());
        assertEquals(parameters.getPercentageCommissionSponsor(), result.getPercentageCommissionSponsor());
    }
}
