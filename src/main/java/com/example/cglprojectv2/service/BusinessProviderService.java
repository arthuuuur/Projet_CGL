package com.example.cglprojectv2.service;

import com.example.cglprojectv2.entity.Business;
import com.example.cglprojectv2.entity.BusinessProvider;
import com.example.cglprojectv2.entity.Commission;
import com.example.cglprojectv2.entity.Parameters;
import com.example.cglprojectv2.entity.datatable.*;
import com.example.cglprojectv2.repository.BusinessProviderRepository;
import com.example.cglprojectv2.service.common.AbstractService;
import com.example.cglprojectv2.service.common.IDatatableService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Service
public class BusinessProviderService extends AbstractService<BusinessProvider, BusinessProviderRepository> implements IDatatableService<BusinessProvider> {

    private static final Comparator<BusinessProvider> EMPTY_COMPARATOR = (e1, e2) -> 0;

    //The number of months we want to get the history of money earned
    private static final int NUMBER_OF_MONTHS_SUM_COMMISSION_HISTORY = 3;
    private final ParametersService parametersService;
    private final UtilService utilService;
    private final CommissionService commissionService;

    /**
     * Constructor
     *
     * @param businessProviderRepository the business provider repository
     * @param parametersService          the parameters service
     * @param utilService                the util service
     * @param commissionService          the commission service
     */
    public BusinessProviderService(BusinessProviderRepository businessProviderRepository, ParametersService parametersService, UtilService utilService, CommissionService commissionService) {
        super(businessProviderRepository);
        this.parametersService = parametersService;
        this.utilService = utilService;
        this.commissionService = commissionService;
    }

    /**
     * Update the business provider with the given id
     *
     * @param id      the id of the business provider
     * @param request the request
     */
    public void updateBusinessProvider(Long id, HttpServletRequest request) {
        Optional<BusinessProvider> businessProvider = this.get(id);
        assert businessProvider.isPresent();
        BusinessProvider businessProviderToUpdate = businessProvider.get();
        businessProviderToUpdate.setFirstName(request.getParameter("businessProviderFirstName"));
        businessProviderToUpdate.setLastName(request.getParameter("businessProviderLastName"));

        String idSponsor = request.getParameter("businessProviderSponsor");
        if (idSponsor != null && !idSponsor.isEmpty()) {
            Optional<BusinessProvider> sponsor = this.get(Long.parseLong(idSponsor));
            assert sponsor.isPresent();
            businessProviderToUpdate.setSponsor(sponsor.get());
        } else {
            businessProviderToUpdate.setSponsor(null);
        }
        repository.save(businessProviderToUpdate);
    }

    /**
     * Delete the business provider with the given id
     *
     * @param id the id of the business provider
     */
    public void deleteBusinessProvider(long id) {
        Optional<BusinessProvider> businessProvider = repository.findById(id);
        assert businessProvider.isPresent();
        businessProvider.get().getSponsored().forEach(bp -> bp.setSponsor(null));
        repository.delete(businessProvider.get());
    }

    /**
     * @param businessProviderToUpdate the business provider to update
     * @return All the business providers that are available to sponsor the given business provider
     */
    public Stream<BusinessProvider> getAvailableSponsors(BusinessProvider businessProviderToUpdate) {
        Parameters parameters = parametersService.getParameters();

        // Remove the current user form the list, remove also all the users that have already the maximum of consecutive sponsor
        // and remove all the users that are already sponsored by the current user
        return this.getAll().
                filter(current ->
                        !Objects.equals(current.getId(), businessProviderToUpdate.getId())
                                && current.countSuccessiveSponsor() < parameters.getMaxSuccessiveSponsor()
                                && !this.checkIfBusinessProviderIsPresentRecursivelly(current, businessProviderToUpdate)
                );
    }

    /**
     * @param businessProvider the business provider to check
     * @param checkIfPresent   the business provider to check if present
     * @return true if the given business provider is present in the tree of the given business provider otherwise false
     */
    public boolean checkIfBusinessProviderIsPresentRecursivelly(BusinessProvider businessProvider, BusinessProvider checkIfPresent) {
        if (businessProvider == null) {
            return false;
        }
        if (businessProvider.getSponsor() == checkIfPresent) {
            return true;
        }
        return checkIfBusinessProviderIsPresentRecursivelly(businessProvider.getSponsor(), checkIfPresent);
    }

    /**
     * check if the business provider has a business in the last X days, where X is the number of days of the parameter
     *
     * @param businessProvider the business provider
     * @return True is the given business provider is affiliated otherwise false
     */

    public boolean isAffiliated(BusinessProvider businessProvider) {

        Parameters parameters = parametersService.getParameters();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -parameters.getAffiliatedDay());

        Set<Business> businessList = businessProvider.getBusinesses();
        List<Business> businessListXDaysAgo = businessList.stream()
                .filter(business -> business.getDate().after(calendar.getTime())).toList();

        return !businessListXDaysAgo.isEmpty();
    }

    /**
     * @param userName the user name to check
     * @return the business provider with the given user name otherwise null
     */
    public BusinessProvider findByUserName(String userName) {
        return repository.findByUserName(userName);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Page getPage(PagingRequest pagingRequest, Object option) {
        List<BusinessProvider> businessProviders = repository.findAll();

        List<List<String>> filtered = businessProviders.stream()
                .sorted(this.sort(pagingRequest, null))
                .filter(this.filter(pagingRequest, null))
                .skip(pagingRequest.getStart())
                .limit(pagingRequest.getLength())
                .map(businessProvider -> this.format(businessProvider, null))
                .toList();

        long count = businessProviders.stream()
                .filter(filter(pagingRequest, null))
                .count();

        Page page = new Page(filtered);
        page.setRecordsFiltered((int) count);
        page.setRecordsTotal((int) count);
        page.setDraw(pagingRequest.getDraw());

        return page;
    }

    /**
     * @param businessProvider the business provider
     * @param numberOfMonths   the number of months
     * @return the sum of the commission of the given business provider in the last X months, where X is the number of months
     */
    public List<Double> getSumCommissionHistory(BusinessProvider businessProvider, int numberOfMonths) {
        List<Double> sumCommissionHistory = new ArrayList<>();
        for (int i = 0; i < numberOfMonths; i++) {
            sumCommissionHistory.add(getSumCommissionForPreviousMonth(businessProvider, -i));
        }
        return sumCommissionHistory;
    }

    /**
     * @param businessProvider the business provider
     * @param previousMonth    the offset of the previous month from the current date
     * @return the sum of the commission of the given business provider in the previous month
     */
    public double getSumCommissionForPreviousMonth(BusinessProvider businessProvider, int previousMonth) {
        Date upperMonth = utilService.getMonthDate(previousMonth + 1);
        Date bottomMonth = utilService.getMonthDate(previousMonth);
        List<Commission> allBusinessProviderCommission = commissionService.findAllByCommissionOwner(businessProvider);
        Predicate<Commission> commissionPredicate = commission -> commission.getBusiness().getDate().after(bottomMonth) && commission.getBusiness().getDate().before(upperMonth);
        return allBusinessProviderCommission.stream().filter(commissionPredicate).mapToDouble(Commission::getAmount).sum();
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<String> format(BusinessProvider businessProvider, Object option) {
        List<Double> sumCommissionMonthHistory = this.getSumCommissionHistory(businessProvider, NUMBER_OF_MONTHS_SUM_COMMISSION_HISTORY);
        List<String> businessProviderStringList = new ArrayList<>();
        businessProviderStringList.add(businessProvider.getFullName());
        businessProviderStringList.add(this.isAffiliated(businessProvider) ? "Oui" : "Non");
        businessProviderStringList.add(businessProvider.getSponsor() != null ? businessProvider.getSponsor().getFullName() : "");
        sumCommissionMonthHistory.forEach(sumCommission -> businessProviderStringList.add(sumCommission + " €"));
        businessProviderStringList.add(Long.toString(businessProvider.getId()));
        businessProviderStringList.add(businessProvider.getSponsor() != null ? Long.toString(businessProvider.getSponsor().getId()) : "");
        return businessProviderStringList;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Predicate<BusinessProvider> filter(PagingRequest pagingRequest, Object option) {
        List<Column> columns = pagingRequest.getColumns();

        return businessProvider -> {
            List<Double> sumCommissionMonthHistory = this.getSumCommissionHistory(businessProvider, NUMBER_OF_MONTHS_SUM_COMMISSION_HISTORY);

            if (!utilService.contains(businessProvider.getFullName(), columns.get(0).getSearch().getValue()))
                return false;
            if (!utilService.contains(this.isAffiliated(businessProvider) ? "oui" : "non", columns.get(1).getSearch().getValue()))
                return false;
            if (!utilService.contains(businessProvider.getSponsor() != null ? businessProvider.getSponsor().getFullName() : "", columns.get(2).getSearch().getValue()))
                return false;
            if (!utilService.contains(String.valueOf(sumCommissionMonthHistory.get(0)), columns.get(3).getSearch().getValue()))
                return false;
            if (!utilService.contains(String.valueOf(sumCommissionMonthHistory.get(1)), columns.get(4).getSearch().getValue()))
                return false;
            return utilService.contains(String.valueOf(sumCommissionMonthHistory.get(2)), columns.get(5).getSearch().getValue());
        };
    }

    /**
     * @inheritDoc
     */
    @Override
    public Comparator<BusinessProvider> sort(PagingRequest pagingRequest, Object option) {
        Order order = pagingRequest.getOrder().get(0);
        int columnIndex = order.getColumn();

        Comparator<BusinessProvider> comparator = EMPTY_COMPARATOR;

        if (columnIndex == 0) {
            comparator = Comparator.comparing(BusinessProvider::getFullName);
        }
        if (columnIndex == 1) {
            comparator = Comparator.comparing(this::isAffiliated);
        }
        if (columnIndex == 2) {
            comparator = Comparator.comparing(businessProvider -> businessProvider.getSponsor() != null ? businessProvider.getSponsor().getFullName() : "");
        }
        if (columnIndex == 3) {
            comparator = Comparator.comparing(businessProvider -> this.getSumCommissionHistory(businessProvider, NUMBER_OF_MONTHS_SUM_COMMISSION_HISTORY).get(0));
        }
        if (columnIndex == 4) {
            comparator = Comparator.comparing(businessProvider -> this.getSumCommissionHistory(businessProvider, NUMBER_OF_MONTHS_SUM_COMMISSION_HISTORY).get(1));
        }
        if (columnIndex == 5) {
            comparator = Comparator.comparing(businessProvider -> this.getSumCommissionHistory(businessProvider, NUMBER_OF_MONTHS_SUM_COMMISSION_HISTORY).get(2));
        }

        if (order.getDir() == Direction.desc) {
            comparator = comparator.reversed();
        }

        return comparator;
    }
}
