package org.example.deviceapi.repository;

import org.example.deviceapi.model.Device;
import org.example.deviceapi.model.DeviceState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device,Long> {

    List<Device> findByBrand(String brand);
    List<Device> findByState(DeviceState state);
}
