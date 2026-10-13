package com.spenvolt.api.service;

import com.spenvolt.api.adapter.MarketplaceAdapter;
import com.spenvolt.api.model.Device;
import com.spenvolt.api.repository.DeviceOfferRepository;
import jakarta.persistence.Id;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class DeviceOfferService {

    private final List<MarketplaceAdapter> adapters;
    private final DeviceOfferRepository repository;

    public DeviceOfferService(
            List<MarketplaceAdapter> adapters,
            DeviceOfferRepository repository
    ) {
        this.adapters = adapters;
        this.repository = repository;
    }

    public List<Device> search(String query) {

        List<Device> results = new ArrayList<>();

        for (MarketplaceAdapter adapter : adapters) {

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

        return results;
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