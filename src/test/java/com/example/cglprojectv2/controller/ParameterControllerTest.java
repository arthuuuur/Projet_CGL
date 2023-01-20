package com.example.cglprojectv2.controller;

import com.example.cglprojectv2.service.ParametersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ParameterController.class)
class ParameterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ParametersService parametersService;

    @Test
    void whenAccessingAnyParameterRoute_givenNonAuthenticatedUser_ThenRedirection() throws Exception {
        mockMvc.perform(get("/parametres/"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void whenAccessingAnyParameterRoute_givenNonAdminUser_ThenForbidden() throws Exception {
        mockMvc.perform(get("/parametres/")).andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void whenShowUpdatePage_thenShowUpdatePage() throws Exception {
        mockMvc.perform(get("/parametres/"))
                .andExpect(status().isOk())
                .andExpect(view().name("parameters/form"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void whenUpdateParameters_givenValidData_thenRedirectToHomePage() throws Exception {
        mockMvc.perform(post("/parametres/")
                        .param("percentageCommissionSponsor", "1.0, 2.0, 3.0")
                        .param("percentageCommission", "0.05")
                        .param("affiliatedDay", "30")
                        .param("maxSuccessiveSponsor", "3")
                        .param("minimumBusinessToBeAffiliated", "1")
                )
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));
    }


    @Test
    @WithMockUser(username = "test.test", roles = {"ADMIN", "USER"})
    void whenUpdateParameters_givenInvalidData_thenRedirectShowUpdate() throws Exception {
        mockMvc.perform(post("/parametres/")
                        .param("percentageCommissionSponsor", "1.0, 2.0, 3.0"))
                .andExpect(view().name("parameters/form"));
    }
}
