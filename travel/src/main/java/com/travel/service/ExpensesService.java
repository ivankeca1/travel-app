package com.travel.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.travel.dto.ExpensesDto;
import com.travel.repository.ExpensesRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ExpensesService {

    private ExpensesRepository expensesRepository;
    private ExpensesMapper expensesMapper;

    public List<ExpensesDto> findAll(){
        return this.expensesMapper.toDtoList(this.expensesRepository.findAll());
    }

    public void saveExpenses(final ExpensesDto expensesDto){
        this.expensesRepository.save(this.expensesMapper.toModel(expensesDto));
    }

    public void deleteById(final long id){
        this.expensesRepository.deleteById(id);
    }

}
