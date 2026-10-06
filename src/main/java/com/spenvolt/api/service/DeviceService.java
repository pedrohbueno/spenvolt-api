package com.spenvolt.api.service;

import com.spenvolt.api.adapter.DeviceAdapter;
import com.spenvolt.api.model.Device;
import com.spenvolt.api.repository.DeviceRepository;
import jakarta.persistence.Id;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class DeviceService {

    private final List<DeviceAdapter> adapters;
    private final DeviceRepository repository;

    public DeviceService(
            List<DeviceAdapter> adapters,
            DeviceRepository repository
    ) {
        this.adapters = adapters;
        this.repository = repository;
    }

    public List<Device> search(String query) {

        List<Device> results = new ArrayList<>();

        for (DeviceAdapter adapter : adapters) {

            if (!adapter.isAvailable()) {
                continue;
            }

            try {
                List<Device> found = adapter.search(query);

                for (Device device : found) {

                    // Se já existe exatamente igual, não salva novamente
                    if (exists(device)) {
                        continue;
                    }

                    Device saved = repository.save(device);

                    results.add(saved);
                }

            } catch (Exception e) {
                System.err.println("Adapter falhou: " + e.getMessage());
            }
        }

        return repository.findAll();
    }

    private boolean exists(Device device) {

        List<Device> devices = repository.findAll();

        for (Device existing : devices) {

            if (sameDevice(device, existing)) {
                return true;
            }
        }

        return false;
    }

    private boolean sameDevice(Device device1, Device device2) {

        try {
            for (Field field : Device.class.getDeclaredFields()) {

                // Ignora somente o ID
                if (field.isAnnotationPresent(Id.class)) {
                    continue;
                }

                field.setAccessible(true);

                if (!Objects.equals(
                        field.get(device1),
                        field.get(device2)
                )) {
                    return false;
                }
            }

            return true;

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}