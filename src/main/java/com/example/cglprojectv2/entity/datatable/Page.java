package com.example.cglprojectv2.entity.datatable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Page {
    public Page(List<List<String>> data){
        this.data = data;
    }
    private List<List<String>> data;
    private int recordsFiltered;
    private int recordsTotal;
    private int draw;
}