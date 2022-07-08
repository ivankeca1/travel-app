package com.travel.service;

import java.io.IOException;
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
    private LoggingService loggingService;

    public List<ExpensesDto> findAll(){
        return this.expensesMapper.toDtoList(this.expensesRepository.findAll());
    }

    public void saveExpenses(final ExpensesDto expensesDto){
        try {
            this.loggingService.writeToLogs("Expenses created.");
            this.expensesRepository.save(this.expensesMapper.toModel(expensesDto));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteById(final long id){
        try {
            this.loggingService.writeToLogs("Expenses with id " + id + " deleted.");
            this.expensesRepository.deleteById(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
