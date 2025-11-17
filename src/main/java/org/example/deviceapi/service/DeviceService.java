package org.example.deviceapi.service;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.deviceapi.dto.DeviceRequest;
import org.example.deviceapi.exception.ApiException;
import org.example.deviceapi.model.Device;
import org.example.deviceapi.model.DeviceState;
import org.example.deviceapi.repository.DeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class DeviceService {

    private final DeviceRepository repo;

    public DeviceService(DeviceRepository repo) { this.repo = repo; }

    public Device create(DeviceRequest req) {
        Device d = new Device();
        d.setName(req.getName());
        d.setBrand(req.getBrand());
        d.setState(req.getState());
        return repo.save(d);
    }

    public Device findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ApiException("Device not found", 404));
    }

    public List<Device> findAll() { return repo.findAll(); }
    public List<Device> findByBrand(String brand) { return repo.findByBrand(brand); }
    public List<Device> findByState(DeviceState state) { return repo.findByState(state); }

    @Transactional
    public Device fullUpdate(Long id, DeviceRequest req) {
        Device existing = findById(id);

        if (existing.getState() == DeviceState.IN_USE) {
            if (!existing.getName().equals(req.getName()) || !existing.getBrand().equals(req.getBrand())) {
                throw new ApiException("Cannot update name or brand while device is in use", 400);
            }
        }

        existing.setName(req.getName());
        existing.setBrand(req.getBrand());
        existing.setState(req.getState());

        return repo.save(existing);
    }

    @Transactional
    public Device partialUpdate(Long id, DeviceRequest req) {
        Device existing = findById(id);

        if (req.getName() != null) {
            if (existing.getState() == DeviceState.IN_USE && !existing.getName().equals(req.getName()))
                throw new ApiException("Cannot update name while device is in use", 400);
            existing.setName(req.getName());
        }

        if (req.getBrand() != null) {
            if (existing.getState() == DeviceState.IN_USE && !existing.getBrand().equals(req.getBrand()))
                throw new ApiException("Cannot update brand while device is in use", 400);
            existing.setBrand(req.getBrand());
        }

        if (req.getState() != null)
            existing.setState(req.getState());

        return repo.save(existing);
    }

    public void delete(Long id) {
        Device existing = findById(id);
        if (existing.getState() == DeviceState.IN_USE)
            throw new ApiException("Cannot delete device while it is in use", 400);
        repo.delete(existing);
    }
}
