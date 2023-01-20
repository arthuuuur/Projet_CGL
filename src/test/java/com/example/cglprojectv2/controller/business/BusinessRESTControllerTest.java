package com.example.cglprojectv2.controller.business;

import com.example.cglprojectv2.entity.Business;
import com.example.cglprojectv2.entity.BusinessProvider;
import com.example.cglprojectv2.entity.datatable.Page;
import com.example.cglprojectv2.entity.datatable.PagingRequest;
import com.example.cglprojectv2.service.business.BusinessService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BusinessRESTController.class)
class BusinessRESTControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BusinessService businessService;

    @Test
    void whenAccessingAnyBusinessRESTControllerRoute_givenNonAuthenticatedUser_thenRedirection() throws Exception {
        mockMvc.perform(get("/api/business/")).andExpect(status().is3xxRedirection());
        mockMvc.perform(get("/api/business/fetch_businesses/")).andExpect(status().is3xxRedirection());
        mockMvc.perform(get("/api/business/fetch_undirect_user_businesses/")).andExpect(status().is3xxRedirection());
        mockMvc.perform(get("/api/business/fetch_direct_user_businesses/")).andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "test.test", roles = {"ADMIN", "USER"})
    void whenGet_givenId_thenOk() throws Exception {
        BusinessProvider businessProvider = BusinessProvider.builder().firstName("Jhon").lastName("Snow").build();

        Business business = Business.builder()
                .name("business")
                .amount(1000.0)
                .businessProvider(businessProvider)
                .date(new Date())
                .build();

        when(businessService.get(anyLong())).thenReturn(Optional.of(business));
        mockMvc.perform(get("/api/business/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test.test", roles = {"ADMIN", "USER"})
    void whenGet_givenUnknowId_thenOk() throws Exception {
        when(businessService.get(anyLong())).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/business/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "test.test", roles = {"ADMIN", "USER"})
    void whenFetchBusinesses_givenPagingRequest_thenOk() throws Exception {
        when(businessService.getBusinnessesArray(any(PagingRequest.class))).thenReturn(new Page());
        mockMvc.perform(post("/api/business/fetch_businesses/")
                        .param("id", "0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new PagingRequest()))
                )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test.test", roles = {"ADMIN", "USER"})
    void whenFetchBusinesses_givenError_then500() throws Exception {
        when(businessService.getBusinnessesArray(any(PagingRequest.class))).thenThrow(new RuntimeException());
        mockMvc.perform(post("/api/business/fetch_businesses/")
                        .param("id", "0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new PagingRequest()))
                )
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = "test.test", roles = {"ADMIN", "USER"})
    void whenFetchUndirectUserBusinesses_givenPagingRequest_thenOk() throws Exception {
        when(businessService.getUndirectUserBusinnessesArray(anyLong(), any(PagingRequest.class))).thenReturn(new Page());
        mockMvc.perform(post("/api/business/fetch_undirect_user_businesses/")
                        .param("id", "0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new PagingRequest()))
                )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test.test", roles = {"ADMIN", "USER"})
    void whenFetchUndirectUserBusinesses_givenError_then500() throws Exception {
        when(businessService.getUndirectUserBusinnessesArray(anyLong(), any(PagingRequest.class))).thenThrow(new RuntimeException());
        mockMvc.perform(post("/api/business/fetch_undirect_user_businesses/")
                        .param("id", "0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new PagingRequest()))
                )
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = "test.test", roles = {"ADMIN", "USER"})
    void whenFetchDirectUserBusinesses_givenPagingRequest_thenOk() throws Exception {
        when(businessService.getDirectUserBusinnessesArray(anyLong(), any(PagingRequest.class))).thenReturn(new Page());
        mockMvc.perform(post("/api/business/fetch_direct_user_businesses/")
                        .param("id", "0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new PagingRequest()))
                )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test.test", roles = {"ADMIN", "USER"})
    void whenFetchDirectUserBusinesses_givenError_then500() throws Exception {
        when(businessService.getDirectUserBusinnessesArray(anyLong(), any(PagingRequest.class))).thenThrow(new RuntimeException());
        mockMvc.perform(post("/api/business/fetch_direct_user_businesses/")
                        .param("id", "0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new PagingRequest()))
                )
                .andExpect(status().isInternalServerError());
    }
}
