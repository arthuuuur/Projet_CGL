package com.example.cglprojectv2.service.business;

import com.example.cglprojectv2.entity.Business;
import com.example.cglprojectv2.entity.BusinessProvider;
import com.example.cglprojectv2.entity.Commission;
import com.example.cglprojectv2.entity.Parameters;
import com.example.cglprojectv2.entity.datatable.Page;
import com.example.cglprojectv2.entity.datatable.PagingRequest;
import com.example.cglprojectv2.repository.BusinessRepository;
import com.example.cglprojectv2.service.BusinessProviderService;
import com.example.cglprojectv2.service.CommissionService;
import com.example.cglprojectv2.service.ParametersService;
import com.example.cglprojectv2.service.UtilService;
import com.example.cglprojectv2.service.business.business_provider_show.DirectBusinessesDatatableService;
import com.example.cglprojectv2.service.business.business_provider_show.UndirectBusinessesDatatableService;
import com.example.cglprojectv2.service.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class BusinessService extends AbstractService<Business, BusinessRepository> {

    private final CommissionService commissionService;
    private final BusinessProviderService businessProviderService;
    private final ParametersService parametersService;
    private final BusinessIndexDatatableService businessIndexDatatableService;
    private final DirectBusinessesDatatableService directBusinessesDatatableService;
    private final UndirectBusinessesDatatableService undirectBusinessesDatatableService;
    private final UtilService utilService;

    /**
     * Constructor
     *
     * @param businessRepository                 the business repository
     * @param commissionService                  the commission service
     * @param businessProviderService            the business provider service
     * @param parametersService                  the parameters service
     * @param businessIndexDatatableService      the business index datatable service
     * @param directBusinessesDatatableService   the direct businesses datatable service
     * @param undirectBusinessesDatatableService the undirect businesses datatable service
     * @param utilService                        the util service
     */
    public BusinessService(BusinessRepository businessRepository, CommissionService commissionService, BusinessProviderService businessProviderService, ParametersService parametersService, BusinessIndexDatatableService businessIndexDatatableService, DirectBusinessesDatatableService directBusinessesDatatableService, UndirectBusinessesDatatableService undirectBusinessesDatatableService, UtilService utilService) {
        super(businessRepository);
        this.commissionService = commissionService;
        this.businessProviderService = businessProviderService;
        this.parametersService = parametersService;
        this.businessIndexDatatableService = businessIndexDatatableService;
        this.directBusinessesDatatableService = directBusinessesDatatableService;
        this.undirectBusinessesDatatableService = undirectBusinessesDatatableService;
        this.utilService = utilService;
    }

    /**
     * Create a new business
     *
     * @param request the request
     * @return the business
     */
    public Business createBusiness(HttpServletRequest request) {
        Business business = new Business();
        business.setName(request.getParameter("businessName"));
        business.setAmount(Double.valueOf(request.getParameter("businessAmount")));

        business.setDate(new Date());

        // Set the business provider to the logged in user
        BusinessProvider businessProvider = businessProviderService.findByUserName(utilService.getLoggedInUserName());
        businessProvider.addBusiness(business);

        Parameters parameters = parametersService.getParameters();

        double sumCommissionSponsor = 0;
        double previousCommission = business.getAmount() * parameters.getPercentageCommission();

        //Create the commissions for the sponsors
        int nbSponsor = businessProvider.countSuccessiveSponsor();
        if (nbSponsor > 0) {
            BusinessProvider currentSponsor = businessProvider.getSponsor();
            for (int i = 1; i <= nbSponsor; i++) {
                if (businessProviderService.isAffiliated(currentSponsor)) {
                    Commission commission = Commission.builder()
                            .commissionOwner(currentSponsor)
                            .percentageCommissionSponsor(parameters.getPercentageCommissionSponsor().get(i - 1))
                            .percentageCommission(parameters.getPercentageCommission())
                            .amount(previousCommission * parameters.getPercentageCommissionSponsor().get(i - 1))
                            .depthFromOriginalProvider(i)
                            .build();

                    business.addCommission(commission);

                    previousCommission = commission.getAmount();
                    sumCommissionSponsor += commission.getAmount();
                }
                currentSponsor = currentSponsor.getSponsor();
            }
        }

        //Create the commission for the original provider
        Commission originalCommission = Commission.builder()
                .commissionOwner(businessProvider)
                .percentageCommission(parameters.getPercentageCommission())
                .amount((business.getAmount() * parameters.getPercentageCommission()) - sumCommissionSponsor)
                .depthFromOriginalProvider(0)
                .build();
        business.addCommission(originalCommission);
        return repository.save(business);
    }

    /**
     * Update a business with the given id
     *
     * @param id      the id of the business to update
     * @param request the request
     */
    @Transactional
    public Business updateBusiness(long id, HttpServletRequest request) {
        Optional<Business> business = this.get(id);
        assert business.isPresent();
        Business businessToUpdate = business.get();
        businessToUpdate.setName(request.getParameter("businessName"));
        businessToUpdate.setAmount(request.getParameter("businessAmount") != null ? Double.parseDouble(request.getParameter("businessAmount")) : 0);

        double sumCommissionSponsor = 0;
        double previousCommission = businessToUpdate.getAmount() * parametersService.getParameters().getPercentageCommission();

        //Update the commissions for the sponsors
        Set<Commission> commissions = businessToUpdate.getCommissions();
        if (commissions != null && !commissions.isEmpty()) {
            List<Commission> orderedCommissions = commissions.stream()
                    .sorted(Comparator.comparingInt(Commission::getDepthFromOriginalProvider))
                    .toList();
            for (Commission commission : orderedCommissions) {
                if (commission.getDepthFromOriginalProvider() > 0) {
                    commission.setAmount(previousCommission * commission.getPercentageCommissionSponsor());
                    previousCommission = commission.getAmount();
                    sumCommissionSponsor += commission.getAmount();
                }
            }
        }

        //Update the commission for the original provider
        Commission originalCommission = businessToUpdate.getCommissionByDepthFromOriginalProvider(0);
        originalCommission.setAmount((businessToUpdate.getAmount() * originalCommission.getPercentageCommission()) - sumCommissionSponsor);

        return repository.save(businessToUpdate);
    }

    /**
     * Delete a business with the given id
     *
     * @param id the id of the business to delete
     */
    public void delete(long id) {
        Optional<Business> business = repository.findById(id);
        assert business.isPresent();
        repository.delete(business.get());
    }

    /**
     * @param pagingRequest the paging request
     * @return The datatable page of businesses
     */
    public Page getBusinnessesArray(PagingRequest pagingRequest) {
        return businessIndexDatatableService.getPage(pagingRequest, null);
    }

    /**
     * @param id            the id of the business provider
     * @param pagingRequest the paging request
     * @return The datatable page of undirect businesses of the business provider with the given id
     */
    public Page getUndirectUserBusinnessesArray(long id, PagingRequest pagingRequest) {
        return undirectBusinessesDatatableService.getPage(pagingRequest, id);
    }

    /**
     * @param id            the id of the business provider
     * @param pagingRequest the paging request
     * @return The datatable page of direct businesses of the business provider with the given id
     */
    public Page getDirectUserBusinnessesArray(long id, PagingRequest pagingRequest) {
        return directBusinessesDatatableService.getPage(pagingRequest, id);
    }

    /**
     * @param businessProvider the business provider
     * @return The total amount of the businesses of the given business provider
     */
    public int countByBusinessProvider(BusinessProvider businessProvider) {
        return repository.countByBusinessProvider(businessProvider);
    }

    /**
     * @param businessProvider the business provider
     * @return The total amount of all the commissions of the given business provider
     */
    public Double getAmountEarnedByBusinessProvider(BusinessProvider businessProvider) {
        List<Commission> commissions = commissionService.findAllByCommissionOwner(businessProvider);
        return commissions.stream().mapToDouble(Commission::getAmount).sum();
    }
}

