package com.example.cglprojectv2.entity.datatable;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Order {
    private Integer column;
    private Direction dir;
}