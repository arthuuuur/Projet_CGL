package com.example.cglprojectv2.controller.business_provider;

import com.example.cglprojectv2.entity.BusinessProvider;
import com.example.cglprojectv2.entity.datatable.Page;
import com.example.cglprojectv2.entity.datatable.PagingRequest;
import com.example.cglprojectv2.service.BusinessProviderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/businessProvider")
public class BusinessProviderRESTController {

    private final BusinessProviderService businessProviderService;

    /**
     * Constructor
     *
     * @param businessProviderService the business provider service
     */
    public BusinessProviderRESTController(BusinessProviderService businessProviderService) {
        this.businessProviderService = businessProviderService;
    }

    /**
     * @param id the id of the business provider
     * @return the business provider
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<BusinessProvider> find(@PathVariable("id") Long id) {
        Optional<BusinessProvider> businessProvider = businessProviderService.get(id);
        return businessProvider.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * @param id the id of the business provider
     * @return All the business providers that are available to sponsor the given business provider
     */
    @GetMapping(value = "/fetch_all_available_sponsors/{id}")
    public ResponseEntity<List<BusinessProvider>> fetchAllAvailableSponsors(@PathVariable("id") Long id) {
        Optional<BusinessProvider> businessProvider = businessProviderService.get(id);
        if (businessProvider.isPresent()) {
            List<BusinessProvider> businessProviders = businessProviderService.getAvailableSponsors(businessProvider.get()).toList();
            if (businessProviders.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(businessProviders);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Fetch all businessProvider for datatable index
     *
     * @param pagingRequest the paging request
     * @return the page of businessProvider
     */
    @PostMapping(value = "/fetch_businessProviders", produces = "application/json")
    public ResponseEntity<Page> fetchBusinessProviders(@RequestBody PagingRequest pagingRequest) {
        try {
            Page page = businessProviderService.getPage(pagingRequest, null);
            return ResponseEntity.ok(page);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }


    }
}
