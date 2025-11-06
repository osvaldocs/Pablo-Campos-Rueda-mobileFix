package com.example.mobileFix.service;

import com.example.mobileFix.model.Device;
import java.util.List;

public interface DeviceService {

    List<Device> getAllDevices();

    Device getDeviceById(Long id);

    Device getDeviceBySerialNumber(String serialNumber);

    List<Device> getDevicesByBrand(String brand);

    Device createDevice(Device device);

    Device updateDevice(Long id, Device device);

    void deleteDevice(Long id);
}
