package com.travel.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExpensesDto {

    private long id;
    private BigDecimal costOfTravel;
    private String nationalCurrency;
    private String dateOfTravel;
    private BigDecimal extraCost;

}
