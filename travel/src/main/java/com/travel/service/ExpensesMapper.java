package com.travel.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.travel.dto.ExpensesDto;
import com.travel.model.Expenses;

@Service
public class ExpensesMapper {

    @Value("${national.currency}")
    private String nationalCurrency;

    public ExpensesDto toDto(final Expenses expenses){
        final ExpensesDto dto = new ExpensesDto();
        dto.setId(expenses.getId());
        dto.setCostOfTravel(expenses.getCostOfTravel());
        dto.setNationalCurrency(this.nationalCurrency);
        dto.setDateOfTravel(expenses.getDateOfTravel());
        dto.setExtraCost(expenses.getExtraCost());

        return dto;
    }

    public Expenses toModel(final ExpensesDto expensesDto){
        final Expenses model = new Expenses();
        model.setId(expensesDto.getId());
        model.setCostOfTravel(expensesDto.getCostOfTravel());
        model.setDateOfTravel(expensesDto.getDateOfTravel());
        model.setExtraCost(expensesDto.getExtraCost());

        if(expensesDto.getNationalCurrency() == null){
            model.setNationalCurrency(this.nationalCurrency);
        } else {
            model.setNationalCurrency(expensesDto.getNationalCurrency());
        }

        return model;
    }

    public List<ExpensesDto> toDtoList(final List<Expenses> expensesList){
        return expensesList.stream().map(this::toDto).collect(Collectors.toList());
    }

}
