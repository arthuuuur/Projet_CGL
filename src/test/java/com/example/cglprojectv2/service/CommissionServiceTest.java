package com.example.cglprojectv2.service;

import com.example.cglprojectv2.entity.BusinessProvider;
import com.example.cglprojectv2.entity.Commission;
import com.example.cglprojectv2.repository.CommissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CommissionServiceTest {

    private CommissionService service;
    private CommissionRepository repository;

    @BeforeEach
    public void setup() {
        repository = mock(CommissionRepository.class);
        service = new CommissionService(repository);
    }

    @Test
    void testFindAllByCommissionOwner() {
        BusinessProvider businessProvider = BusinessProvider.builder().id(1L).firstName("Test").lastName("Provider").build();
        List<Commission> commissions = Arrays.asList(
                Commission.builder().id(1L).amount(10.0).depthFromOriginalProvider(0).commissionOwner(businessProvider).build(),
                Commission.builder().id(2L).amount(20.0).depthFromOriginalProvider(1).commissionOwner(businessProvider).build(),
                Commission.builder().id(3L).amount(30.0).depthFromOriginalProvider(2).commissionOwner(businessProvider).build()
        );
        when(repository.findAllByCommissionOwner(businessProvider)).thenReturn(commissions);
        List<Commission> result = service.findAllByCommissionOwner(businessProvider);

        assertEquals(3, result.size());
        assertEquals(commissions, result);

        when(repository.findAllByCommissionOwner(businessProvider)).thenReturn(List.of());
        result = service.findAllByCommissionOwner(businessProvider);

        assertEquals(0, result.size());
    }

}
