package com.example.cglprojectv2.service.common;

import com.example.cglprojectv2.entity.datatable.Page;
import com.example.cglprojectv2.entity.datatable.PagingRequest;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public interface IDatatableService<T> {

    /**
     * @param pagingRequest the paging request
     * @param option any options you want to pass
     * @return the datatable page entity
     */
    Page getPage(PagingRequest pagingRequest, Object option);

    /**
     * @param entity the entity you want to format
     * @param option any options you want to pass
     * @return the formatted entity
     */
    List<String> format(T entity, Object option);

    /**
     * @param pagingRequest the paaging request
     * @param option any options you want to pass
     * @return return the predcate to perform filtering
     */
    Predicate<T> filter(PagingRequest pagingRequest, Object option);

    /**
     * @param pagingRequest the paging request
     * @param option any options you want to pass
     * @return the comparator to perform sorting
     */
    Comparator<T> sort(PagingRequest pagingRequest, Object option);

}
