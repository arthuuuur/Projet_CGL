package com.example.cglprojectv2.service.business.business_provider_show;

import com.example.cglprojectv2.entity.Business;
import com.example.cglprojectv2.entity.BusinessProvider;
import com.example.cglprojectv2.entity.Commission;
import com.example.cglprojectv2.entity.datatable.*;
import com.example.cglprojectv2.service.BusinessProviderService;
import com.example.cglprojectv2.service.CommissionService;
import com.example.cglprojectv2.service.UtilService;
import com.example.cglprojectv2.service.common.IDatatableService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class UndirectBusinessesDatatableService implements IDatatableService<Business> {
    private static final Comparator<Business> EMPTY_COMPARATOR = (e1, e2) -> 0;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy à HH:mm:ss");
    private final BusinessProviderService businessProviderService;
    private final CommissionService commissionService;
    private final UtilService utilService;

    /**
     * Constructor
     *
     * @param businessProviderService the business provider service
     * @param commissionService       the commission service
     * @param utilService             the util service
     */
    public UndirectBusinessesDatatableService(BusinessProviderService businessProviderService, CommissionService commissionService, UtilService utilService) {
        this.businessProviderService = businessProviderService;
        this.commissionService = commissionService;
        this.utilService = utilService;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Page getPage(PagingRequest pagingRequest, Object option) {
        Optional<BusinessProvider> user = businessProviderService.get((Long) option);
        assert user.isPresent();

        List<Commission> commissions = commissionService.findAllByCommissionOwner(user.get());
        List<Business> businesses = commissions.stream().map(Commission::getBusiness).filter(business -> business.getBusinessProvider() != user.get()).toList();

        List<List<String>> filtered = businesses.stream()
                .sorted(sort(pagingRequest, user.get()))
                .filter(filter(pagingRequest, user.get()))
                .skip(pagingRequest.getStart())
                .limit(pagingRequest.getLength())
                .map(b -> this.format(b, user.get()))
                .toList();

        long count = businesses.stream()
                .filter(filter(pagingRequest, user.get()))
                .count();

        Page page = new Page(filtered);
        page.setRecordsFiltered((int) count);
        page.setRecordsTotal((int) count);
        page.setDraw(pagingRequest.getDraw());

        return page;
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<String> format(Business business, Object option) {

        Optional<Commission> commissionOriginalProvider = business.getCommissions()
                .stream()
                .filter(commission -> commission.getDepthFromOriginalProvider() == 0).toList().stream().findFirst();

        Optional<Commission> commissionCurrentUser = business.getCommissions()
                .stream()
                .filter(commission -> commission.getCommissionOwner().equals(option)).toList().stream().findFirst();

        return Arrays.asList(
                business.getName(),
                business.getBusinessProviderFullName(),
                business.getAmount() + " €",
                commissionOriginalProvider.map(commission -> commission.getAmount() + "€").orElse("probleme pour récupérer la commission"),
                commissionCurrentUser.map(commission -> commission.getAmount() + "€").orElse("probleme pour récupérer la commission"),
                sdf.format(business.getDate()),
                Long.toString(business.getId()),
                Long.toString(business.getBusinessProvider().getId())
        );
    }

    /**
     * @inheritDoc
     */
    @Override
    public Predicate<Business> filter(PagingRequest pagingRequest, Object option) {
        List<Column> columns = pagingRequest.getColumns();

        return business -> {
            Optional<Commission> commissionOriginalProvider = business.getCommissions()
                    .stream()
                    .filter(commission -> commission.getDepthFromOriginalProvider() == 0).toList().stream().findFirst();

            Optional<Commission> commissionCurrentUser = business.getCommissions()
                    .stream()
                    .filter(commission -> commission.getCommissionOwner().equals(option)).toList().stream().findFirst();

            if (!utilService.contains(business.getName(), columns.get(0).getSearch().getValue())) return false;
            if (!utilService.contains(business.getBusinessProviderFullName(), columns.get(1).getSearch().getValue()))
                return false;
            if (!utilService.contains(business.getAmount() + " €", columns.get(2).getSearch().getValue())) return false;
            if (!utilService.contains(commissionOriginalProvider.map(commission -> commission.getAmount() + "€").orElse(null), columns.get(3).getSearch().getValue()))
                return false;
            if (!utilService.contains(commissionCurrentUser.map(commission -> commission.getAmount() + "€").orElse(null), columns.get(4).getSearch().getValue()))
                return false;
            return utilService.contains(sdf.format(business.getDate()), columns.get(5).getSearch().getValue());
        };
    }

    /**
     * @inheritDoc
     */
    @Override
    public Comparator<Business> sort(PagingRequest pagingRequest, Object option) {

        Order order = pagingRequest.getOrder().get(0);
        int columnIndex = order.getColumn();

        Comparator<Business> comparator = EMPTY_COMPARATOR;

        if (columnIndex == 0) {
            comparator = Comparator.comparing(Business::getName);
        } else if (columnIndex == 1) {
            comparator = Comparator.comparing(business -> business.getBusinessProvider().getFullName());
        } else if (columnIndex == 2) {
            comparator = Comparator.comparing(Business::getAmount);
        } else if (columnIndex == 3) {
            comparator = Comparator.comparing(business -> {
                Optional<Commission> commissions = business.getCommissions()
                        .stream()
                        .filter(commission -> commission.getDepthFromOriginalProvider() == 0).toList().stream().findFirst();
                assert commissions.isPresent();
                return commissions.get().getAmount();
            });
        } else if (columnIndex == 4) {
            comparator = Comparator.comparing(business -> {
                Optional<Commission> commissions = business.getCommissions()
                        .stream()
                        .filter(commission -> commission.getCommissionOwner().equals(option)).toList().stream().findFirst();
                assert commissions.isPresent();
                return commissions.get().getAmount();
            });
        } else if (columnIndex == 5) {
            comparator = Comparator.comparing(Business::getDate);
        }

        if (order.getDir() == Direction.desc) {
            comparator = comparator.reversed();
        }

        return comparator;
    }
}
