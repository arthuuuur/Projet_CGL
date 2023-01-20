package com.example.cglprojectv2.service;

import com.example.cglprojectv2.entity.Business;
import com.example.cglprojectv2.entity.BusinessProvider;
import com.example.cglprojectv2.entity.Commission;
import com.example.cglprojectv2.entity.Parameters;
import com.example.cglprojectv2.repository.BusinessProviderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BusinessProviderServiceTest {

    private BusinessProviderService service;
    private BusinessProviderRepository repository;
    private ParametersService parametersService;
    private UtilService utilService;
    private CommissionService commissionService;

    @BeforeEach
    void init() {
        repository = mock(BusinessProviderRepository.class);
        parametersService = mock(ParametersService.class);
        utilService = new UtilService();
        commissionService = mock(CommissionService.class);
        service = new BusinessProviderService(repository, parametersService, utilService, commissionService);
    }

    @Test
    void testUpdateBusinessProvider_withValidIdAndValidParams() {
        BusinessProvider businessProvider = BusinessProvider.builder().firstName("John").lastName("Doe").sponsored(new HashSet<>()).build();

        when(service.get(1L)).thenReturn(Optional.of(businessProvider));

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("businessProviderFirstName")).thenReturn("Jane");
        when(request.getParameter("businessProviderLastName")).thenReturn("Smith");
        when(request.getParameter("businessProviderSponsor")).thenReturn("2");

        BusinessProvider sponsor = BusinessProvider.builder().firstName("Jane").lastName("Smith").sponsored(new HashSet<>()).build();

        when(service.get(2L)).thenReturn(Optional.of(sponsor));

        service.updateBusinessProvider(1L, request);

        verify(repository).save(argThat(bp -> bp.getFirstName().equals("Jane") && bp.getLastName().equals("Smith") && bp.getSponsor().equals(sponsor)));
    }

    @Test
    void testUpdateBusinessProvider_withValidIdAndInvalidParams() {
        BusinessProvider businessProvider = BusinessProvider.builder().firstName("John").lastName("Doe").sponsored(new HashSet<>()).build();

        when(service.get(1L)).thenReturn(Optional.of(businessProvider));

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("businessProviderFirstName")).thenReturn(null);
        when(request.getParameter("businessProviderLastName")).thenReturn("");
        when(request.getParameter("businessProviderSponsor")).thenReturn("invalid");

        try {
            service.updateBusinessProvider(1L, request);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertNotNull(ex);
        }

        verify(repository, never()).save(any());
    }

    @Test
    void testUpdateBusinessProvider_withInvalidId() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("businessProviderFirstName")).thenReturn(null);
        when(request.getParameter("businessProviderLastName")).thenReturn("");
        when(request.getParameter("businessProviderSponsor")).thenReturn("invalid");

        try {
            service.updateBusinessProvider(-1L, request);
            fail("Expected an AssertionError to be thrown");
        } catch (AssertionError e) {
            assertNotNull(e);
        }
    }

    @Test
    void testDeleteBusinessProvider_withValidId() {

        BusinessProvider businessProvider1 = BusinessProvider.builder().firstName("John").lastName("Doe").sponsored(new HashSet<>()).build();
        BusinessProvider businessProvider = BusinessProvider.builder().firstName("John").lastName("Doe").sponsored(new HashSet<>()).build();
        businessProvider1.setSponsor(businessProvider);
        businessProvider.getSponsored().add(businessProvider1);

        when(repository.findById(1L)).thenReturn(Optional.of(businessProvider));
        service.deleteBusinessProvider(1L);

        verify(repository, times(1)).delete(businessProvider);
        assertNull(businessProvider1.getSponsor());
    }

    @Test
    void testDeleteBusinessProvider_withInvalidId() {
        Optional<BusinessProvider> businessProvider = Optional.of(new BusinessProvider());
        businessProvider.get().setSponsored(new HashSet<>());

        try {
            service.deleteBusinessProvider(1L);
            fail("Expected an AssertionError to be thrown");
        } catch (AssertionError e) {
            assertNotNull(e);
        }

        verify(repository, times(0)).delete(businessProvider.get());
    }

    @Test
    void testGetAvailableSponsors() {
        BusinessProvider businessProviderToUpdate = BusinessProvider.builder().id(3L).build();

        List<BusinessProvider> allBusinessProviders = new ArrayList<>();
        BusinessProvider bp1 = BusinessProvider.builder().id(2L).sponsor(null).build();
        BusinessProvider bp2 = BusinessProvider.builder().id(3L).sponsor(null).build();
        BusinessProvider bp3 = BusinessProvider.builder().id(4L).sponsor(bp1).build();
        BusinessProvider bp4 = BusinessProvider.builder().id(5L).sponsor(bp3).build();
        BusinessProvider bp5 = BusinessProvider.builder().id(6L).sponsor(bp4).build();
        BusinessProvider bp6 = BusinessProvider.builder().id(7L).sponsor(bp5).build();
        BusinessProvider bp7 = BusinessProvider.builder().id(8L).sponsor(bp6).build();

        allBusinessProviders.add(bp1);
        allBusinessProviders.add(bp2);
        allBusinessProviders.add(bp3);
        allBusinessProviders.add(bp4);
        allBusinessProviders.add(bp5);
        allBusinessProviders.add(bp6);
        allBusinessProviders.add(bp7);

        Parameters parameters = Parameters.builder().maxSuccessiveSponsor(3).build();

        when(repository.findAll()).thenReturn(allBusinessProviders);
        when(parametersService.getParameters()).thenReturn(parameters);

        Stream<BusinessProvider> result = service.getAvailableSponsors(businessProviderToUpdate);

        assertEquals(result.toList(), Arrays.asList(bp1, bp3, bp4));
    }

    @Test
    void testCheckIfBusinessProviderIsPresentRecursivelly() {
        BusinessProvider bp7 = BusinessProvider.builder().build();
        BusinessProvider bp6 = BusinessProvider.builder().sponsor(bp7).build();
        BusinessProvider bp5 = BusinessProvider.builder().sponsor(bp6).build();
        BusinessProvider bp4 = BusinessProvider.builder().sponsor(null).build();
        BusinessProvider bp3 = BusinessProvider.builder().sponsor(bp4).build();
        BusinessProvider bp2 = BusinessProvider.builder().sponsor(bp3).build();
        BusinessProvider bp1 = BusinessProvider.builder().sponsor(bp2).build();

        assertTrue(service.checkIfBusinessProviderIsPresentRecursivelly(bp1, bp3));
        assertFalse(service.checkIfBusinessProviderIsPresentRecursivelly(bp1, bp6));
        assertTrue(service.checkIfBusinessProviderIsPresentRecursivelly(bp1, bp4));
        assertTrue(service.checkIfBusinessProviderIsPresentRecursivelly(bp5, bp7));
        assertFalse(service.checkIfBusinessProviderIsPresentRecursivelly(bp3, bp1));
    }

    @Test
    void testIsAffiliated_NoBusinesses() {
        BusinessProvider businessProvider = new BusinessProvider();
        businessProvider.setBusinesses(new HashSet<>());

        Parameters parameters = Parameters.builder().affiliatedDay(60).build();

        when(parametersService.getParameters()).thenReturn(parameters);

        boolean result = service.isAffiliated(businessProvider);

        assertFalse(result);
    }

    @Test
    void testIsAffiliated_BusinesseOld() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -90);

        Business business1 = Business.builder().date(calendar.getTime()).build();
        BusinessProvider businessProvider = BusinessProvider.builder().businesses(new HashSet<>(Collections.singletonList(business1))).build();
        Parameters parameters = Parameters.builder().affiliatedDay(60).build();

        when(parametersService.getParameters()).thenReturn(parameters);

        assertFalse(service.isAffiliated(businessProvider));
    }

    @Test
    void testIsAffiliated_BusinesseRecent() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -40);

        Business business1 = Business.builder().date(calendar.getTime()).build();
        BusinessProvider businessProvider = BusinessProvider.builder().businesses(new HashSet<>(Collections.singletonList(business1))).build();
        Parameters parameters = Parameters.builder().affiliatedDay(60).build();

        when(parametersService.getParameters()).thenReturn(parameters);

        assertTrue(service.isAffiliated(businessProvider));
    }

    @Test
    void testFindByUserName_Exists() {
        String userName = "test_user";
        BusinessProvider expectedBusinessProvider = BusinessProvider.builder().userName("test_user").build();

        when(repository.findByUserName(userName)).thenReturn(expectedBusinessProvider);
        BusinessProvider result = service.findByUserName(userName);
        assertEquals(expectedBusinessProvider, result);

        when(repository.findByUserName(userName)).thenReturn(null);
        result = service.findByUserName(userName);
        assertNull(result);
    }

    @Test
    void testSumCommissionHistory() {
        BusinessProvider businessProvider = BusinessProvider.builder().build();
        Commission commission1 = Commission.builder().amount(10.0).build();
        Commission commission2 = Commission.builder().amount(20.0).build();
        Commission commission3 = Commission.builder().amount(30.0).build();
        Commission commission4 = Commission.builder().amount(40.0).build();
        Commission commission5 = Commission.builder().amount(50.0).build();

        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        calendar.add(Calendar.MONTH, -1);
        Date date1MonthAgo = calendar.getTime();
        calendar.add(Calendar.MONTH, -2);
        Date date3MonthAgo = calendar.getTime();
        calendar.add(Calendar.MONTH, -1);
        Date date4MonthAgo = calendar.getTime();

        Business business1 = Business.builder().commissions(Set.of(commission1)).date(currentDate).build();
        Business business2 = Business.builder().commissions(Set.of(commission2)).date(date1MonthAgo).build();
        Business business3 = Business.builder().commissions(Set.of(commission3)).date(date3MonthAgo).build();
        Business business4 = Business.builder().commissions(Set.of(commission4)).date(date3MonthAgo).build();
        Business business5 = Business.builder().commissions(Set.of(commission5)).date(date4MonthAgo).build();

        commission1.setBusiness(business1);
        commission2.setBusiness(business2);
        commission3.setBusiness(business3);
        commission4.setBusiness(business4);
        commission5.setBusiness(business5);

        businessProvider.setBusinesses(new HashSet<>(Arrays.asList(business1, business2, business3, business4, business5)));

        when(commissionService.findAllByCommissionOwner(businessProvider)).thenReturn(List.of(commission1, commission2, commission3, commission4, commission5));

        List<Double> result = service.getSumCommissionHistory(businessProvider, 4);
        System.out.println(result);
        assertEquals(4, result.size());
        assertEquals(10, result.get(0));
        assertEquals(20, result.get(1));
        assertEquals(0, result.get(2));
        assertEquals(70, result.get(3));
    }

}
