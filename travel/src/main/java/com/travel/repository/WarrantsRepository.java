package com.travel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.travel.model.Warrant;

@Repository
public interface WarrantsRepository extends PagingAndSortingRepository<Warrant, Long> {

    @Query(value = "SELECT * FROM warrants", nativeQuery = true)
    List<Warrant> findALlWarrants();

}
