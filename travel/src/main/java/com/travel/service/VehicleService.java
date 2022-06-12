package com.travel.service;

import com.travel.dto.DecryptedVehicleDto;
import com.travel.dto.VehicleDto;
import com.travel.model.Vehicle;
import com.travel.repository.VehicleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VehicleService {

    private VehicleRepository vehicleRepository;
    private VehicleMapper vehicleMapper;

    public List<Vehicle> findAll(){
        return this.vehicleRepository.findAll();
    }

    public VehicleDto saveVehicle(final VehicleDto vehicleDto){
        return this.vehicleMapper.toDto(this.vehicleRepository.save(this.vehicleMapper.toModel(vehicleDto)));
    }

    public void deleteById(final long id){
        this.vehicleRepository.deleteById(id);
    }

    public DecryptedVehicleDto decryptRegistrationPlateById(final long id) {
        final Optional<Vehicle> vehicle = this.vehicleRepository.findById(id);
        return vehicle.map(value -> this.vehicleMapper.toDecryptedVehicleRegistrationDto(value)).orElse(null);


    }

}
