package com.example.cglprojectv2.controller;

import com.example.cglprojectv2.entity.BusinessProvider;
import com.example.cglprojectv2.service.BusinessProviderService;
import com.example.cglprojectv2.service.UtilService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Controller
public class HomeController {

    private final UtilService utilService;
    private final BusinessProviderService businessProviderService;

    /**
     * Constructor
     *
     * @param utilService             the util service
     * @param businessProviderService the business provider service
     */
    public HomeController(UtilService utilService, BusinessProviderService businessProviderService) {
        this.utilService = utilService;
        this.businessProviderService = businessProviderService;
    }

    /**
     * application entry point
     *
     * @return the home page
     */
    @GetMapping(value = "/")
    public String showWelcomePage() {
        fakeCheckUserLikeLDAP();
        return "redirect:affaires/";
    }

    /**
     * To simulate a LDAP
     * Imagine that user have to login with firstname.lastname as username
     * We check if the user exist in the database
     * If not, we create it (if it existe in the active directory of the company)
     * To simulate the active directory we define 4 user in the SecurityConfiguration class
     */
    public void fakeCheckUserLikeLDAP() {
        String username = utilService.getLoggedInUserName();
        BusinessProvider user = businessProviderService.findByUserName(username);
        if (user == null && !Objects.equals(username, "admin")) { // if the user doesn't exist in our database we imagine that we check if the user is present in the AD (active directory) if yes we add it to our db.
            String[] name = username.split("\\.");
            BusinessProvider businessProvider = BusinessProvider.builder().firstName(name[0]).lastName(name[1]).userName(username).build();
            businessProviderService.save(businessProvider);
        }
    }

    /*
     * Auto generated method
     */
    @GetMapping(value = "/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return "redirect:/";
    }
}
