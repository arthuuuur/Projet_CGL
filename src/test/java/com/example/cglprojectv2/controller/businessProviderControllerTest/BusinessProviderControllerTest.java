package com.example.cglprojectv2.controller.businessProviderControllerTest;

import com.example.cglprojectv2.controller.business_provider.BusinessProviderController;
import com.example.cglprojectv2.entity.BusinessProvider;
import com.example.cglprojectv2.service.BusinessProviderService;
import com.example.cglprojectv2.service.UtilService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BusinessProviderController.class)
class BusinessProviderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BusinessProviderService businessProviderService;
    @MockBean
    private UtilService utilService;

    @Test
    void WhenAccessingAnyBusinessProviderRoute_givenNonAuthenticatedUser_ThenRedirection() throws Exception {
        mockMvc.perform(get("/apporteur-affaire/")).andExpect(status().is3xxRedirection());
        mockMvc.perform(get("/apporteur-affaire/afficher")).andExpect(status().is3xxRedirection());
        mockMvc.perform(get("/apporteur-affaire/modifier")).andExpect(status().is3xxRedirection());
        mockMvc.perform(get("/apporteur-affaire/supprimer")).andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void whenAccessingDeleteRoute_givenNonAdminUser_ThenForbidden() throws Exception {
        mockMvc.perform(post("/apporteur-affaire/supprimer")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void whenShowIndex_thenIndex() throws Exception {
        mockMvc.perform(get("/apporteur-affaire/"))
                .andExpect(view().name("businessProvider/index"));
    }

    @Test
    @WithMockUser(username = "test.test", roles = {"ADMIN", "USER"})
    void whenShow_givenUnknowId_thenReturn404() throws Exception {
        when(businessProviderService.get(anyLong())).thenReturn(Optional.empty());
        mockMvc.perform(get("/apporteur-affaire/afficher")
                        .param("id", "1"))
                .andExpect(view().name("error/404"));
    }

    @Test
    @WithMockUser(username = "test.test", roles = {"ADMIN", "USER"})
    void whenShow_givenId_thenReturnView() throws Exception {
        when(businessProviderService.get(anyLong())).thenReturn(Optional.of(new BusinessProvider()));
        mockMvc.perform(get("/apporteur-affaire/afficher")
                        .param("id", "1"))
                .andExpect(view().name("businessProvider/view"));
    }

    @Test
    @WithMockUser(username = "test.test", roles = {"ADMIN", "USER"})
    void whenUpdate_GivenId_thenRedirectIndex() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("businessProviderId")).thenReturn(String.valueOf(anyLong()));
        mockMvc.perform(post("/apporteur-affaire/modifier").param("businessProviderId", String.valueOf(anyLong())))
                .andExpect(view().name("redirect:/apporteur-affaire/"));
    }

    @Test
    @WithMockUser(username = "test.test", roles = {"ADMIN", "USER"})
    void whenUpdate_givenUnknowId_thenReturn404() throws Exception {
        mockMvc.perform(post("/apporteur-affaire/modifier?businessProviderId="))
                .andExpect(view().name("error/404"));
    }

    @Test
    @WithMockUser(username = "test.test", roles = {"ADMIN", "USER"})
    void whenDelete_GivenId_thenRedirectIndex() throws Exception {
        mockMvc.perform(post("/apporteur-affaire/supprimer").param("entityId", String.valueOf(anyLong())))
                .andExpect(view().name("redirect:/apporteur-affaire/"));
    }

    @Test
    @WithMockUser(username = "test.test", roles = {"ADMIN", "USER"})
    void whenDelete_givenUnknowId_thenReturn404() throws Exception {
        when(businessProviderService.get(anyLong())).thenReturn(Optional.empty());
        mockMvc.perform(post("/apporteur-affaire/supprimer?entityId="))
                .andExpect(view().name("error/404"));
    }
}
