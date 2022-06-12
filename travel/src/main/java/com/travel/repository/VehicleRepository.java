package com.travel.repository;

import com.travel.model.Vehicle;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends PagingAndSortingRepository<Vehicle, Long> {

    List<Vehicle> findAll();
    void deleteById(Long id);


}
