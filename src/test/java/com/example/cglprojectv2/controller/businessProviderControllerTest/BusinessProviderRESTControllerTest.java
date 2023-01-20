package com.example.cglprojectv2.controller.businessProviderControllerTest;

import com.example.cglprojectv2.controller.business_provider.BusinessProviderRESTController;
import com.example.cglprojectv2.entity.BusinessProvider;
import com.example.cglprojectv2.entity.datatable.Page;
import com.example.cglprojectv2.entity.datatable.PagingRequest;
import com.example.cglprojectv2.service.BusinessProviderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BusinessProviderRESTController.class)
class BusinessProviderRESTControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BusinessProviderService businessProviderService;

    @Test
    void whenAccessingAnyBusinessProviderRESTControllerRoute_givenNonAuthenticatedUser_thenRedirection() throws Exception {
        mockMvc.perform(get("/api/businessProvider")).andExpect(status().is3xxRedirection());
        mockMvc.perform(get("/api/businessProvider/fetch_all_available_sponsors/")).andExpect(status().is3xxRedirection());
        mockMvc.perform(get("/api/businessProvider/fetch_businessProviders")).andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "test.test", roles = {"ADMIN", "USER"})
    void whenGet_givenId_thenOk() throws Exception {
        when(businessProviderService.get(anyLong())).thenReturn(Optional.of(new BusinessProvider()));
        mockMvc.perform(get("/api/businessProvider/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test.test", roles = {"ADMIN", "USER"})
    void whenGet_givenUnknowId_thenNotFound() throws Exception {
        when(businessProviderService.get(anyLong())).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/businessProvider/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "test.test", roles = {"ADMIN", "USER"})
    void whenFetchAllAvailableSponsor_givenId_thenReturnOk() throws Exception {
        when(businessProviderService.get(anyLong())).thenReturn(Optional.of(new BusinessProvider()));
        when(businessProviderService.getAvailableSponsors(any(BusinessProvider.class))).thenReturn(Stream.of(new BusinessProvider()));

        mockMvc.perform(get("/api/businessProvider/fetch_all_available_sponsors/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test.test", roles = {"ADMIN", "USER"})
    void whenFetchAllAvailableSponsor_givenIdNoAvailaibleSponsor_thenReturnNoContent() throws Exception {
        when(businessProviderService.get(anyLong())).thenReturn(Optional.of(new BusinessProvider()));
        when(businessProviderService.getAvailableSponsors(any(BusinessProvider.class))).thenReturn(Stream.empty());
        mockMvc.perform(get("/api/businessProvider/fetch_all_available_sponsors/" + anyLong()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "test.test", roles = {"ADMIN", "USER"})
    void whenFetchAllAvailableSponsor_givenUnknowId_thenReturnNoContent() throws Exception {
        when(businessProviderService.get(anyLong())).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/businessProvider/fetch_all_available_sponsors/" + anyLong()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "test.test", roles = {"ADMIN", "USER"})
    void whenFetchBusinessProviders_givenPagingRequest_thenOk() throws Exception {
        when(businessProviderService.getPage(any(PagingRequest.class), isNull())).thenReturn(new Page());
        mockMvc.perform(post("/api/businessProvider/fetch_businessProviders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new PagingRequest()))
                )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test.test", roles = {"ADMIN", "USER"})
    void whenFetchBusinessProviders_givenError_then500() throws Exception {
        when(businessProviderService.getPage(any(PagingRequest.class), isNull())).thenThrow(new RuntimeException());
        mockMvc.perform(post("/api/businessProvider/fetch_businessProviders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new PagingRequest()))
                )
                .andExpect(status().isInternalServerError());
    }

}
