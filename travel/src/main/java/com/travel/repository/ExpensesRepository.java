package com.travel.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.travel.model.Expenses;

@Repository
public interface ExpensesRepository extends PagingAndSortingRepository<Expenses, Long> {

    Expenses findById(long id);
    List<Expenses> findAll();

}
