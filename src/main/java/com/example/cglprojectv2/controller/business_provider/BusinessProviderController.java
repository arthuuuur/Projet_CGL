package com.example.cglprojectv2.controller.business_provider;

import com.example.cglprojectv2.entity.BusinessProvider;
import com.example.cglprojectv2.service.BusinessProviderService;
import com.example.cglprojectv2.service.UtilService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequestMapping("/apporteur-affaire")
public class BusinessProviderController {

    private static final String ERROR_404 = "error/404";

    /**
     * Business provider service
     */
    private final BusinessProviderService businessProviderService;

    /**
     * Util service
     */
    private final UtilService utilService;

    /**
     * Constructor
     *
     * @param businessProviderService the service for businessProvider entity
     * @param utilService             the service for util method
     */
    public BusinessProviderController(BusinessProviderService businessProviderService, UtilService utilService) {
        this.businessProviderService = businessProviderService;
        this.utilService = utilService;
    }

    /**
     * @return the view of all business providers
     */
    @GetMapping(value = "/")
    public String index() {
        return "businessProvider/index";
    }

    /**
     * @param model the model
     * @return the view of the businessProvider if it exists, the view of 404 error otherwise
     */
    @GetMapping(value = "/afficher")
    public String view(@RequestParam long id, ModelMap model) {
        Optional<BusinessProvider> businessProvider = businessProviderService.get(id);
        Optional<BusinessProvider> loggedUser = Optional.ofNullable(businessProviderService.findByUserName(utilService.getLoggedInUserName()));

        if (businessProvider.isEmpty()) {
            return ERROR_404;
        }

        if (loggedUser.isPresent()) {
            model.addAttribute("loggedUserId", loggedUser.get().getId());
        } else {
            model.addAttribute("loggedUserId", -1);
        }

        model.addAttribute("businessProvider", businessProvider.get());
        return "businessProvider/view";

    }

    /**
     * @param request the request
     * @return the view of all businessProvider or the view of 404 error if no parameter id is found
     */
    @PostMapping(value = "/modifier")
    public String update(HttpServletRequest request) {
        String id = request.getParameter("businessProviderId");

        if (id == null || id.isEmpty()) {
            return ERROR_404;
        }

        businessProviderService.updateBusinessProvider(Long.parseLong(id), request);
        return "redirect:/apporteur-affaire/";
    }

    /**
     * @param request the request
     * @return the view of all businessProvider
     */
    @PostMapping(value = "/supprimer")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(HttpServletRequest request) {
        String id = request.getParameter("entityId");

        if (id == null || id.isEmpty()) {
            return ERROR_404;
        }

        businessProviderService.deleteBusinessProvider(Long.parseLong(id));
        return "redirect:/apporteur-affaire/";
    }
}

