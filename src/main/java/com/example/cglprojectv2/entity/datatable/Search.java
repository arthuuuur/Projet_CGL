package com.example.cglprojectv2.entity.datatable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class Search {
    private String value;
    private String regexp;
}