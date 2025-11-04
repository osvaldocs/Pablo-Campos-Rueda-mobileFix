package com.example.mobileFix.repository;

import com.example.mobileFix.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    // Buscar un dispositivo por número de serie (opcional único)
    Optional<Device> findBySerialNumber(String serialNumber);

    // Métodos adicionales útiles para tests o filtrado
    List<Device> findByBrand(String brand);
    List<Device> findByModel(String model);
    List<Device> findByBrandAndModel(String brand, String model);
}
