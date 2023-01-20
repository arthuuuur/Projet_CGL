package com.example.cglprojectv2.controller;

import com.example.cglprojectv2.service.BusinessProviderService;
import com.example.cglprojectv2.service.UtilService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UtilService utilService;

    @MockBean
    private BusinessProviderService businessProviderService;

    @Test
    void whenAccessingAnyHomeControllerRoute_givenNonAuthenticatedUser_ThenRedirection() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "test.test", roles = {"ADMIN", "USER"})
    void whenIndex_thenReturnIndex() throws Exception {
        //Why the @WithMockUser don't work for this method, so username is null in fakeCheckUserLikeLDAP method :(
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }
}
