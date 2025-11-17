package org.example.deviceapi.dto;

import lombok.*;
import org.example.deviceapi.model.DeviceState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public class DeviceRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String brand;

    @NotNull
    private DeviceState state;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public DeviceState getState() {
        return state;
    }

    public void setState(DeviceState state) {
        this.state = state;
    }
}
