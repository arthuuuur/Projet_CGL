package com.example.cglprojectv2.service;

import com.example.cglprojectv2.entity.Business;
import com.example.cglprojectv2.entity.BusinessProvider;
import com.example.cglprojectv2.entity.Commission;
import com.example.cglprojectv2.entity.Parameters;
import com.example.cglprojectv2.repository.BusinessProviderRepository;
import com.example.cglprojectv2.repository.BusinessRepository;
import com.example.cglprojectv2.service.business.BusinessIndexDatatableService;
import com.example.cglprojectv2.service.business.BusinessService;
import com.example.cglprojectv2.service.business.business_provider_show.DirectBusinessesDatatableService;
import com.example.cglprojectv2.service.business.business_provider_show.UndirectBusinessesDatatableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class BusinessServiceTest {

    private BusinessService businessService;
    private HttpServletRequest request;
    private BusinessProviderService businessProviderService;
    private UtilService utilService;
    private ParametersService parametersService;

    @Autowired
    private BusinessRepository repository;
    @Autowired
    private BusinessProviderRepository businessProviderRepository;

    @BeforeEach
    public void setUp() {
        request = mock(HttpServletRequest.class);
        businessProviderService = mock(BusinessProviderService.class);
        utilService = mock(UtilService.class);
        parametersService = mock(ParametersService.class);
        CommissionService commissionService = mock(CommissionService.class);
        BusinessIndexDatatableService businessIndexDatatableService = mock(BusinessIndexDatatableService.class);
        DirectBusinessesDatatableService directBusinessesDatatableService = mock(DirectBusinessesDatatableService.class);
        UndirectBusinessesDatatableService undirectBusinessesDatatableService = mock(UndirectBusinessesDatatableService.class);
        businessService = new BusinessService(repository, commissionService, businessProviderService, parametersService, businessIndexDatatableService, directBusinessesDatatableService, undirectBusinessesDatatableService, utilService);
    }

    @Test
    void testCreateBusiness() {
        when(request.getParameter("businessName")).thenReturn("Test Business");
        when(request.getParameter("businessAmount")).thenReturn("1000");
        when(utilService.getLoggedInUserName()).thenReturn("testuser");
        BusinessProvider businessProvider = BusinessProvider.builder().firstName("testA").lastName("userA").userName("testA.userA").build();
        BusinessProvider sponsor1 = BusinessProvider.builder().firstName("testB").lastName("userB").userName("testB.userB").build();
        BusinessProvider sponsor2 = BusinessProvider.builder().firstName("testC").lastName("userC").userName("testC.userC").build();
        businessProvider.setSponsor(sponsor1);
        sponsor1.setSponsor(sponsor2);
        businessProviderRepository.save(sponsor2);
        businessProviderRepository.save(sponsor1);
        businessProviderRepository.save(businessProvider);

        when(businessProviderService.findByUserName("testuser")).thenReturn(businessProvider);
        when(businessProviderService.isAffiliated(sponsor1)).thenReturn(true);
        when(businessProviderService.isAffiliated(sponsor2)).thenReturn(true);
        Parameters parameters = new Parameters();
        parameters.getPercentageCommissionSponsor().add(0.1);
        parameters.getPercentageCommissionSponsor().add(0.2);
        parameters.setPercentageCommission(0.3);
        when(parametersService.getParameters()).thenReturn(parameters);

        Business business = businessService.createBusiness(request);
        assertEquals("Test Business", business.getName());
        assertEquals(1000.0, business.getAmount(), 0.01);
        assertNotNull(business.getDate());
        assertEquals(businessProvider, business.getBusinessProvider());
        assertNotNull(business.getId());

        List<Commission> commissions = business.getCommissions().stream().toList();

        Commission commission1 = commissions.stream().filter(c -> c.getCommissionOwner().equals(sponsor1)).findFirst().get();
        Commission commission2 = commissions.stream().filter(c -> c.getCommissionOwner().equals(sponsor2)).findFirst().get();
        Commission commission3 = commissions.stream().filter(c -> c.getCommissionOwner().equals(businessProvider)).findFirst().get();

        assertEquals(3, commissions.size());

        assertEquals(0.2, commission2.getPercentageCommissionSponsor());
        assertEquals(0.3, commission2.getPercentageCommission());
        assertEquals(6.0, commission2.getAmount());
        assertEquals(2, commission2.getDepthFromOriginalProvider());

        assertEquals(0.1, commission1.getPercentageCommissionSponsor());
        assertEquals(0.3, commission1.getPercentageCommission());
        assertEquals(30.0, commission1.getAmount());
        assertEquals(1, commission1.getDepthFromOriginalProvider());

        assertEquals(0.3, commission3.getPercentageCommission());
        assertEquals(264, commission3.getAmount());
        assertEquals(0, commission3.getDepthFromOriginalProvider());
    }

    @Test
    void testCreateBusinessWithNoSponsor() {
        when(request.getParameter("businessName")).thenReturn("Test Business");
        when(request.getParameter("businessAmount")).thenReturn("1000");
        when(utilService.getLoggedInUserName()).thenReturn("testuser");
        BusinessProvider businessProvider = BusinessProvider.builder().firstName("testA").lastName("userA").userName("testA.userA").build();
        businessProviderRepository.save(businessProvider);

        when(businessProviderService.findByUserName("testuser")).thenReturn(businessProvider);
        Parameters parameters = new Parameters();
        parameters.setPercentageCommission(0.3);
        when(parametersService.getParameters()).thenReturn(parameters);

        Business business = businessService.createBusiness(request);
        assertEquals("Test Business", business.getName());
        assertEquals(1000.0, business.getAmount());
        assertNotNull(business.getDate());
        assertEquals(businessProvider, business.getBusinessProvider());
        assertNotNull(business.getId());

        List<Commission> commissions = business.getCommissions().stream().toList();

        Commission commission1 = commissions.stream().filter(c -> c.getCommissionOwner().equals(businessProvider)).findFirst().get();

        assertEquals(1, commissions.size());

        assertEquals(0.3, commission1.getPercentageCommission());
        assertEquals(300, commission1.getAmount());
        assertEquals(0, commission1.getDepthFromOriginalProvider());
    }

    @Test
    void testUpdateBusiness() {
        when(request.getParameter("businessName")).thenReturn("Test Business");
        when(request.getParameter("businessAmount")).thenReturn("1000");
        when(utilService.getLoggedInUserName()).thenReturn("testuser");
        BusinessProvider businessProvider = BusinessProvider.builder().firstName("testA").lastName("userA").userName("testA.userA").build();
        BusinessProvider sponsor1 = BusinessProvider.builder().firstName("testB").lastName("userB").userName("testB.userB").build();
        BusinessProvider sponsor2 = BusinessProvider.builder().firstName("testC").lastName("userC").userName("testC.userC").build();
        businessProvider.setSponsor(sponsor1);
        sponsor1.setSponsor(sponsor2);
        businessProviderRepository.save(sponsor2);
        businessProviderRepository.save(sponsor1);
        businessProviderRepository.save(businessProvider);

        when(businessProviderService.findByUserName("testuser")).thenReturn(businessProvider);
        when(businessProviderService.isAffiliated(sponsor1)).thenReturn(true);
        when(businessProviderService.isAffiliated(sponsor2)).thenReturn(true);
        Parameters parameters = new Parameters();
        parameters.getPercentageCommissionSponsor().add(0.1);
        parameters.getPercentageCommissionSponsor().add(0.2);
        parameters.setPercentageCommission(0.3);
        when(parametersService.getParameters()).thenReturn(parameters);

        Business business = businessService.createBusiness(request);

        when(request.getParameter("businessAmount")).thenReturn("2000");
        Business updatedBusiness = businessService.updateBusiness(business.getId(), request);

        assertEquals("Test Business", updatedBusiness.getName());
        assertEquals(2000.0, updatedBusiness.getAmount());
        assertNotNull(updatedBusiness.getDate());
        assertEquals(businessProvider, updatedBusiness.getBusinessProvider());
        assertNotNull(updatedBusiness.getId());

        List<Commission> commissions = updatedBusiness.getCommissions().stream().toList();

        Commission commission1 = commissions.stream().filter(c -> c.getCommissionOwner().equals(sponsor1)).findFirst().get();
        Commission commission2 = commissions.stream().filter(c -> c.getCommissionOwner().equals(sponsor2)).findFirst().get();
        Commission commission3 = commissions.stream().filter(c -> c.getCommissionOwner().equals(businessProvider)).findFirst().get();

        assertEquals(3, commissions.size());

        assertEquals(0.2, commission2.getPercentageCommissionSponsor());
        assertEquals(0.3, commission2.getPercentageCommission());
        assertEquals(12.0, commission2.getAmount());
        assertEquals(2, commission2.getDepthFromOriginalProvider());

        assertEquals(0.1, commission1.getPercentageCommissionSponsor());
        assertEquals(0.3, commission1.getPercentageCommission());
        assertEquals(60.0, commission1.getAmount());
        assertEquals(1, commission1.getDepthFromOriginalProvider());

        assertEquals(0.3, commission3.getPercentageCommission());
        assertEquals(528.0, commission3.getAmount());
        assertEquals(0, commission3.getDepthFromOriginalProvider());
    }
}
