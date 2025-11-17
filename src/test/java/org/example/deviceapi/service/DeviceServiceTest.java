package org.example.deviceapi.service;

import org.example.deviceapi.dto.DeviceRequest;
import org.example.deviceapi.exception.ApiException;
import org.example.deviceapi.model.Device;
import org.example.deviceapi.model.DeviceState;
import org.example.deviceapi.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeviceServiceTest {

    @Mock
    private DeviceRepository repo;

    @InjectMocks
    private DeviceService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateDevice() {
        DeviceRequest req = new DeviceRequest();
        req.setName("Device1");
        req.setBrand("BrandA");
        req.setState(DeviceState.AVAILABLE);

        Device saved = new Device();
        saved.setName("Device1");
        saved.setBrand("BrandA");
        saved.setState(DeviceState.AVAILABLE);

        when(repo.save(any(Device.class))).thenReturn(saved);

        Device result = service.create(req);

        assertNotNull(result);
        assertEquals("Device1", result.getName());
        assertEquals("BrandA", result.getBrand());
        verify(repo, times(1)).save(any(Device.class));
    }

    @Test
    void testFindByIdSuccess() {
        Device device = new Device();
        device.setId(1L);

        when(repo.findById(1L)).thenReturn(Optional.of(device));

        Device result = service.findById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void testFindByIdNotFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());
        ApiException ex = assertThrows(ApiException.class, () -> service.findById(1L));
        assertEquals("Device not found", ex.getMessage());
        assertEquals(404, ex.getStatus());
    }

    @Test
    void testFullUpdateSuccess() {
        Device existing = new Device();
        existing.setId(1L);
        existing.setName("OldName");
        existing.setBrand("OldBrand");
        existing.setState(DeviceState.AVAILABLE);

        DeviceRequest req = new DeviceRequest();
        req.setName("NewName");
        req.setBrand("NewBrand");
        req.setState(DeviceState.AVAILABLE);

        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(existing)).thenReturn(existing);

        Device updated = service.fullUpdate(1L, req);
        assertEquals("NewName", updated.getName());
        assertEquals("NewBrand", updated.getBrand());
    }

    @Test
    void testFullUpdateInUseThrowsException() {
        Device existing = new Device();
        existing.setId(1L);
        existing.setName("OldName");
        existing.setBrand("OldBrand");
        existing.setState(DeviceState.IN_USE);

        DeviceRequest req = new DeviceRequest();
        req.setName("NewName");
        req.setBrand("OldBrand");
        req.setState(DeviceState.IN_USE);

        when(repo.findById(1L)).thenReturn(Optional.of(existing));

        ApiException ex = assertThrows(ApiException.class, () -> service.fullUpdate(1L, req));
        assertEquals("Cannot update name or brand while device is in use", ex.getMessage());
    }

    @Test
    void testPartialUpdateSuccess() {
        Device existing = new Device();
        existing.setId(1L);
        existing.setName("OldName");
        existing.setBrand("OldBrand");
        existing.setState(DeviceState.AVAILABLE);

        DeviceRequest req = new DeviceRequest();
        req.setName("NewName");  // only updating name

        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(existing)).thenReturn(existing);

        Device updated = service.partialUpdate(1L, req);
        assertEquals("NewName", updated.getName());
        assertEquals("OldBrand", updated.getBrand());
    }

    @Test
    void testPartialUpdateInUseThrowsException() {
        Device existing = new Device();
        existing.setId(1L);
        existing.setName("OldName");
        existing.setBrand("OldBrand");
        existing.setState(DeviceState.IN_USE);

        DeviceRequest req = new DeviceRequest();
        req.setName("NewName");

        when(repo.findById(1L)).thenReturn(Optional.of(existing));

        ApiException ex = assertThrows(ApiException.class, () -> service.partialUpdate(1L, req));
        assertEquals("Cannot update name while device is in use", ex.getMessage());
    }

    @Test
    void testDeleteSuccess() {
        Device existing = new Device();
        existing.setId(1L);
        existing.setState(DeviceState.AVAILABLE);

        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(repo).delete(existing);

        service.delete(1L);
        verify(repo, times(1)).delete(existing);
    }

    @Test
    void testDeleteInUseThrowsException() {
        Device existing = new Device();
        existing.setId(1L);
        existing.setState(DeviceState.IN_USE);

        when(repo.findById(1L)).thenReturn(Optional.of(existing));

        ApiException ex = assertThrows(ApiException.class, () -> service.delete(1L));
        assertEquals("Cannot delete device while it is in use", ex.getMessage());
    }
}
