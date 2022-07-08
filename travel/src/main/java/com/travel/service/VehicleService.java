package com.travel.service;

import com.travel.dto.DecryptedVehicleDto;
import com.travel.dto.VehicleDto;
import com.travel.model.Vehicle;
import com.travel.repository.VehicleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VehicleService {

    private VehicleRepository vehicleRepository;
    private VehicleMapper vehicleMapper;
    private LoggingService loggingService;

    public List<Vehicle> findAll() {
        return this.vehicleRepository.findAll();
    }

    public VehicleDto saveVehicle(final VehicleDto vehicleDto) {
        try {
            this.loggingService.writeToLogs("Vehicle created.");
            return this.vehicleMapper.toDto(this.vehicleRepository.save(this.vehicleMapper.toModel(vehicleDto)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteById(final long id) {
        try {
            this.vehicleRepository.deleteById(id);
            this.loggingService.writeToLogs("Vehicle with id " + id + " deleted.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public DecryptedVehicleDto decryptRegistrationPlateById(final long id) {
        final Optional<Vehicle> vehicle = this.vehicleRepository.findById(id);
        return vehicle.map(value -> this.vehicleMapper.toDecryptedVehicleRegistrationDto(value)).orElse(null);
    }

    public VehicleDto findById(long id) {
        final Optional<Vehicle> vehicle = this.vehicleRepository.findById(id);
        if (vehicle.isPresent()) {
            return this.vehicleMapper.toDto(vehicle.get());
        }
        return new VehicleDto();
    }
}
