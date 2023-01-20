package com.example.cglprojectv2.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UtilsServiceTest {

    private UtilService utilService;

    @BeforeEach
    void init(){
        this.utilService = new UtilService();
    }

    @Test
    void whenGetMonthDate_givenNegativeOffset_thenOK() {
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        Date monthDate = utilService.getMonthDate( -1);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(monthDate);
        int month = calendar.get(Calendar.MONTH);
        if(currentMonth == 0) {
            assertEquals(11, month);
        } else {
            assertEquals(currentMonth - 1, month);
        }
    }

    @Test
    void whenGetMonthDate_givenOffset_thenOK() {
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        Date monthDate = utilService.getMonthDate( 1);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(monthDate);
        int month = calendar.get(Calendar.MONTH);

        if(currentMonth == 11) {
            assertEquals(0, month);
        } else {
            assertEquals(currentMonth + 1, month);
        }
    }

    @Test
    void whenContains_givenValue_thenOK() {
        Assertions.assertTrue(utilService.contains("test", "test"));
        Assertions.assertFalse(utilService.contains("test", "test2"));
        Assertions.assertTrue(utilService.contains("test", null));
        Assertions.assertTrue(utilService.contains("test", ""));
        Assertions.assertTrue(utilService.contains("test", "TEST"));
    }

    @Test
    void testGetLoggedInUserName_returnsUsername() {
        UserDetails user = new User("test_user", "password", List.of(new SimpleGrantedAuthority("ROLE_USER")));

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));

        String username = utilService.getLoggedInUserName();

        assertEquals("test_user", username);
    }

    @Test
    void testGetLoggedInUserName_returnsNull() {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("non-user-details", null));

        String username = utilService.getLoggedInUserName();

        assertNull(username);
    }
}
