package com.my.webstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCartonDto {
    private int id;
    private int noOfUnits;
    private BigDecimal price;
}
