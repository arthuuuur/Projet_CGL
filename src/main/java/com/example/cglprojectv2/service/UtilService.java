package com.example.cglprojectv2.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class UtilService {

    /**
     * @param monthOffset the number of month to add to the current date
     * @return a Date object representing the first day of the month
     */
    public Date getMonthDate(int monthOffset) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, monthOffset);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * @return the logged in user name
     */
    public String getLoggedInUserName() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }

        return null;
    }

    /**
     * @param container the container to check
     * @param value     the value to check
     * @return if value is not null or empty, return true if the container contains the value or false otherwise
     */
    public boolean contains(String container, String value) {
        if (value != null && !value.isEmpty()) {
            return container.toLowerCase().contains(value.toLowerCase());
        }
        return true;
    }
}
