package com.example.cglprojectv2.controller.business;

import com.example.cglprojectv2.entity.Business;
import com.example.cglprojectv2.service.business.BusinessService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BusinessController.class)
class BusinessControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BusinessService businessService;

    @Test
    void WhenAccessingAnyBusinessRoute_givenNonAuthenticatedUser_ThenRedirection() throws Exception {
        mockMvc.perform(get("/affaires/")).andExpect(status().is3xxRedirection());
        mockMvc.perform(get("/affaires/afficher")).andExpect(status().is3xxRedirection());
        mockMvc.perform(get("/affaires/modifier")).andExpect(status().is3xxRedirection());
        mockMvc.perform(get("/affaires/supprimer")).andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void whenAccessingUpdateAndDeleteRoute_givenNonAdminUser_ThenForbidden() throws Exception {
        mockMvc.perform(post("/affaires/modifier")).andExpect(status().isForbidden());
        mockMvc.perform(post("/affaires/supprimer")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void whenShowIndex_returnIndex() throws Exception {
        mockMvc.perform(get("/affaires/"))
                .andExpect(view().name("business/index"));
    }

    @Test
    @WithMockUser(username = "test.test", roles = {"ADMIN", "USER"})
    void whenShow_givenId_thenOk() throws Exception {
        when(businessService.get(anyLong())).thenReturn(Optional.of(new Business()));
        mockMvc.perform(get("/affaires/afficher")
                        .param("id", String.valueOf(anyLong())))
                .andExpect(view().name("business/view"));
    }

    @Test
    @WithMockUser(username = "test.test", roles = {"ADMIN", "USER"})
    void whenShow_givenUnknowId_then404() throws Exception {
        when(businessService.get(anyLong())).thenReturn(Optional.empty());
        mockMvc.perform(get("/affaires/afficher")
                        .param("id", String.valueOf(anyLong())))
                .andExpect(view().name("error/404"));
    }

    @Test
    @WithMockUser(username = "test.test", roles = {"ADMIN", "USER"})
    void whenCreate_gienCorrectData_thenRedirectIndex() throws Exception {
        when(businessService.createBusiness(any(HttpServletRequest.class))).thenReturn(new Business());
        mockMvc.perform(post("/affaires/creer"))
                .andExpect(view().name("redirect:/affaires/"));
    }

    @Test
    @WithMockUser(username = "test.test", roles = {"ADMIN", "USER"})
    void whenCreate_gienIncorrectData_thenRedirectIndex() throws Exception {
        when(businessService.createBusiness(any(HttpServletRequest.class))).thenReturn(null);
        mockMvc.perform(post("/affaires/creer"))
                .andExpect(view().name("error/500"));
    }


    @Test
    @WithMockUser(username = "test.test", roles = {"ADMIN", "USER"})
    void whenUpdate_givenId_thenOk() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("businessId")).thenReturn(String.valueOf(anyLong()));
        mockMvc.perform(post("/affaires/modifier").param("businessId", String.valueOf(anyLong())))
                .andExpect(view().name("redirect:/affaires/"));
    }

    @Test
    @WithMockUser(username = "test.test", roles = {"ADMIN", "USER"})
    void whenUpdate_givenUnknowId_then404() throws Exception {
        mockMvc.perform(post("/affaires/modifier?businessId="))
                .andExpect(view().name("error/404"));
    }

    @Test
    @WithMockUser(username = "test.test", roles = {"ADMIN", "USER"})
    void whenDelete_givenId_thenOk() throws Exception {
        mockMvc.perform(post("/affaires/supprimer").param("entityId", String.valueOf(anyLong())))
                .andExpect(view().name("redirect:/affaires/"));
    }

    @Test
    @WithMockUser(username = "test.test", roles = {"ADMIN", "USER"})
    void whenDelete_givenUnknowId_then404() throws Exception {
        mockMvc.perform(post("/affaires/supprimer?entityId="))
                .andExpect(view().name("error/404"));
    }
}
