package com.example.cglprojectv2.controller;

import com.example.cglprojectv2.service.StatisticService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(StatisticController.class)
class StatisticControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StatisticService statisticService;

    @Test
    void whenAccessingAnyStatisticRoute_givenNonAuthenticatedUser_ThenRedirection() throws Exception {
        mockMvc.perform(get("/statistique/")).andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void whenAccessingAnyStatisticRoute_givenNonAdminUser_ThenForbidden() throws Exception {
        mockMvc.perform(get("/statistique/")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "test.test", roles = {"ADMIN", "USER"})
    void whenShowStatistic_thenReturnIndex() throws Exception {
        mockMvc.perform(get("/statistique/"))
                .andExpect(status().isOk())
                .andExpect(view().name("statistic/index"));
    }
}
