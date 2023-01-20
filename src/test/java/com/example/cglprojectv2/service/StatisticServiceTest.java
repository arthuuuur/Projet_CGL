package com.example.cglprojectv2.service;

import com.example.cglprojectv2.entity.BusinessProvider;
import com.example.cglprojectv2.service.business.BusinessService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StatisticServiceTest {

    private StatisticService service;
    private BusinessProviderService businessProviderService;
    private BusinessService businessService;

    @BeforeEach
    public void setup() {
        businessProviderService = mock(BusinessProviderService.class);
        businessService = mock(BusinessService.class);
        service = new StatisticService(businessProviderService, businessService);
    }

    @Test
    void testGetNumberOfBusinessForEachBusinessProvider() throws JSONException {
        List<BusinessProvider> businessProviders = Arrays.asList(
                BusinessProvider.builder().id(1L).firstName("Provider").lastName("1").build(),
                BusinessProvider.builder().id(2L).firstName("Provider").lastName("2").build(),
                BusinessProvider.builder().id(3L).firstName("Provider").lastName("3").build()
        );

        when(businessProviderService.getAll()).thenReturn(businessProviders.stream());
        when(businessService.countByBusinessProvider(businessProviders.get(0))).thenReturn(10);
        when(businessService.countByBusinessProvider(businessProviders.get(1))).thenReturn(20);
        when(businessService.countByBusinessProvider(businessProviders.get(2))).thenReturn(30);

        JSONObject data = service.getNumberOfBusinessForEachBusinessProvider();

        assertEquals(3, data.getJSONArray("labels").length());
        assertEquals("Provider 1", data.getJSONArray("labels").getString(0));
        assertEquals("Provider 2", data.getJSONArray("labels").getString(1));
        assertEquals("Provider 3", data.getJSONArray("labels").getString(2));
        assertEquals(3, data.getJSONArray("values").length());
        assertEquals(10, data.getJSONArray("values").getInt(0));
        assertEquals(20, data.getJSONArray("values").getInt(1));
        assertEquals(30, data.getJSONArray("values").getInt(2));

        when(businessProviderService.getAll()).thenReturn(Stream.empty());

        data = service.getNumberOfBusinessForEachBusinessProvider();

        assertEquals(0, data.getJSONArray("labels").length());
        assertEquals(0, data.getJSONArray("values").length());
    }

    @Test
    void getAmountEarnedByBusinessProvider () {
        List<BusinessProvider> businessProviders = Arrays.asList(
                BusinessProvider.builder().id(1L).firstName("Provider").lastName("1").build(),
                BusinessProvider.builder().id(2L).firstName("Provider").lastName("2").build()
        );

        when(businessProviderService.getAll()).thenReturn(businessProviders.stream());
        when(businessService.getAmountEarnedByBusinessProvider(businessProviders.get(0))).thenReturn(100.0);
        when(businessService.getAmountEarnedByBusinessProvider(businessProviders.get(1))).thenReturn(200.0);

        StatisticService service = new StatisticService(businessProviderService, businessService);
        JSONObject result = service.getAmountEarnedByBusinessProvider();

        assertEquals(2, result.getJSONArray("labels").length());
        assertEquals(2, result.getJSONArray("values").length());
        assertEquals("Provider 1", result.getJSONArray("labels").getString(0));
        assertEquals("Provider 2", result.getJSONArray("labels").getString(1));
        assertEquals(100.0, result.getJSONArray("values").getDouble(0));
        assertEquals(200.0, result.getJSONArray("values").getDouble(1));
    }
}
