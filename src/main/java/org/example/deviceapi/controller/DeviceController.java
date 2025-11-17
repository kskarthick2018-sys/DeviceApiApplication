package org.example.deviceapi.controller;

import org.example.deviceapi.dto.DeviceRequest;
import org.example.deviceapi.dto.DeviceResponse;
import org.example.deviceapi.model.Device;
import org.example.deviceapi.model.DeviceState;
import org.example.deviceapi.service.DeviceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    private final DeviceService service;
    public DeviceController(DeviceService service) { this.service = service; }

    private DeviceResponse toResponse(Device d) {
        DeviceResponse r = new DeviceResponse();
        r.setId(d.getId());
        r.setName(d.getName());
        r.setBrand(d.getBrand());
        r.setState(d.getState());
        r.setCreationTime(d.getCreationTime());
        return r;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeviceResponse create(@Valid @RequestBody DeviceRequest req) {
        return toResponse(service.create(req));
    }

    @GetMapping
    public List<DeviceResponse> getAll(@RequestParam(required = false) String brand,
                                       @RequestParam(required = false) DeviceState state) {
        List<Device> list;
        if (brand != null) list = service.findByBrand(brand);
        else if (state != null) list = service.findByState(state);
        else list = service.findAll();
        return list.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public DeviceResponse getOne(@PathVariable Long id) {
        return toResponse(service.findById(id));
    }

    @PutMapping("/{id}")
    public DeviceResponse fullUpdate(@PathVariable Long id, @Valid @RequestBody DeviceRequest req) {
        return toResponse(service.fullUpdate(id, req));
    }

    @PatchMapping("/{id}")
    public DeviceResponse partialUpdate(@PathVariable Long id, @RequestBody DeviceRequest req) {
        return toResponse(service.partialUpdate(id, req));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
