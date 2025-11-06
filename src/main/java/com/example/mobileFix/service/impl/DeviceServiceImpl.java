package com.example.mobileFix.service.impl;

import com.example.mobileFix.model.Device;
import com.example.mobileFix.repository.DeviceRepository;
import com.example.mobileFix.service.DeviceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    @Override
    public Device getDeviceById(Long id) {
        return deviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Device not found with id: " + id));
    }

    @Override
    public Device getDeviceBySerialNumber(String serialNumber) {
        return deviceRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new RuntimeException("Device not found with serial: " + serialNumber));
    }

    @Override
    public List<Device> getDevicesByBrand(String brand) {
        return deviceRepository.findByBrand(brand);
    }

    @Override
    public Device createDevice(Device device) {
        validateDevice(device);

        // Verificar duplicado por serial si no es nulo
        if (device.getSerialNumber() != null &&
                deviceRepository.findBySerialNumber(device.getSerialNumber()).isPresent()) {
            throw new RuntimeException("Device with serial number already exists");
        }

        return deviceRepository.save(device);
    }

    @Override
    public Device updateDevice(Long id, Device device) {
        Device existing = getDeviceById(id);
        validateDevice(device);

        existing.setBrand(device.getBrand());
        existing.setModel(device.getModel());
        existing.setSerialNumber(device.getSerialNumber());

        return deviceRepository.save(existing);
    }

    @Override
    public void deleteDevice(Long id) {
        Device existing = getDeviceById(id);
        deviceRepository.delete(existing);
    }

    private void validateDevice(Device device) {
        if (device.getBrand() == null || device.getBrand().trim().length() < 2) {
            throw new IllegalArgumentException("Brand must have at least 2 characters");
        }
        if (device.getModel() == null || device.getModel().trim().length() < 2) {
            throw new IllegalArgumentException("Model must have at least 2 characters");
        }
    }
}
