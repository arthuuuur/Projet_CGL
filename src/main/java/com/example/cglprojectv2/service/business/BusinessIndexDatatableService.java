package com.example.cglprojectv2.service.business;

import com.example.cglprojectv2.entity.Business;
import com.example.cglprojectv2.entity.Commission;
import com.example.cglprojectv2.entity.datatable.*;
import com.example.cglprojectv2.repository.BusinessRepository;
import com.example.cglprojectv2.service.UtilService;
import com.example.cglprojectv2.service.common.AbstractService;
import com.example.cglprojectv2.service.common.IDatatableService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class BusinessIndexDatatableService extends AbstractService<Business, BusinessRepository> implements IDatatableService<Business> {
    private static final Comparator<Business> EMPTY_COMPARATOR = (e1, e2) -> 0;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy à HH:mm:ss");
    private final UtilService utilService;

    /**
     * Constructor
     *
     * @param businessRepository The business repository
     * @param utilService        The util service
     */
    public BusinessIndexDatatableService(BusinessRepository businessRepository, UtilService utilService) {
        super(businessRepository);
        this.utilService = utilService;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Page getPage(PagingRequest pagingRequest, Object option) {
        List<Business> businesses = repository.findAll();

        List<List<String>> filtered = businesses.stream()
                .sorted(sort(pagingRequest, null))
                .filter(filter(pagingRequest, null))
                .skip(pagingRequest.getStart())
                .limit(pagingRequest.getLength())
                .map(business -> format(business, null))
                .toList();

        long count = businesses.stream()
                .filter(filter(pagingRequest, null))
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

        Optional<Commission> commissions = business.getCommissions()
                .stream()
                .filter(commission -> commission.getDepthFromOriginalProvider() == 0).toList().stream().findFirst();

        return Arrays.asList(
                business.getName(),
                business.getBusinessProviderFullName(),
                business.getAmount() + " €",
                commissions.map(commission -> commission.getAmount() + " €").orElse("probleme pour récupérer la commission"),
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
            Optional<Commission> commissions = business.getCommissions()
                    .stream()
                    .filter(commission -> commission.getDepthFromOriginalProvider() == 0).toList().stream().findFirst();

            if (!utilService.contains(business.getName(), columns.get(0).getSearch().getValue())) return false;
            if (!utilService.contains(business.getBusinessProviderFullName(), columns.get(1).getSearch().getValue()))
                return false;
            if (!utilService.contains(business.getAmount().toString(), columns.get(2).getSearch().getValue()))
                return false;
            if (!utilService.contains(commissions.map(commission -> commission.getAmount().toString()).orElse(null), columns.get(3).getSearch().getValue()))
                return false;
            return utilService.contains(sdf.format(business.getDate()), columns.get(4).getSearch().getValue());
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
        }
        if (columnIndex == 1) {
            comparator = Comparator.comparing(business -> business.getBusinessProvider().getFullName());
        }
        if (columnIndex == 2) {
            comparator = Comparator.comparing(Business::getAmount);
        }
        if (columnIndex == 3) {
            comparator = Comparator.comparing(business -> business.getCommissions().stream()
                    .filter(commission -> commission.getDepthFromOriginalProvider() == 0).toList().stream().findFirst().get().getAmount());
        }
        if (columnIndex == 4) {
            comparator = Comparator.comparing(Business::getDate);
        }

        if (order.getDir() == Direction.desc) {
            comparator = comparator.reversed();
        }

        return comparator;
    }
}
