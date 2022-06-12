package com.travel.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.travel.dto.WarrantDto;
import com.travel.model.Warrant;
import com.travel.repository.ExpensesRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WarrantsMapper implements Runnable {

    private ExpensesRepository expensesRepository;

    //Multithreading
    public WarrantDto toDto(final Warrant warrant){
        final WarrantDto dto = new WarrantDto();

        final Thread idThread = new Thread(() -> dto.setId(warrant.getId()));
        final Thread creationDateThread = new Thread(() -> dto.setCreationDate(warrant.getCreationDate()));
        final Thread warrantStatusThread = new Thread(() -> dto.setWarrantStatus(warrant.getWarrantStatus()));
        final Thread expensesThread = new Thread(() -> dto.setExpensesId(warrant.getExpenses().getId()));

        expensesThread.start();
        System.out.println("Expenses thread started.");
        idThread.start();
        System.out.println("Id thread started.");
        creationDateThread.start();
        System.out.println("Creation date thread started.");
        warrantStatusThread.start();
        System.out.println("Warrant status thread started.");

        try {
            expensesThread.join();
            idThread.join();
            creationDateThread.join();
            warrantStatusThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return dto;
    }

    public Warrant toModel(final WarrantDto warrantDto){
        final Warrant model = new Warrant();

        model.setId(warrantDto.getId());
        model.setCreationDate(warrantDto.getCreationDate());
        model.setWarrantStatus(warrantDto.getWarrantStatus());
        model.setExpenses(this.expensesRepository.findById(warrantDto.getExpensesId()));

        return model;
    }

    public List<WarrantDto> toDtoList(final List<Warrant> aLlWarrants) {
        return aLlWarrants.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public void run() {

    }

}
