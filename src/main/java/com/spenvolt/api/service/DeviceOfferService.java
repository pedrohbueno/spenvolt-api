package com.spenvolt.api.service;

import com.spenvolt.api.adapter.MarketplaceAdapter;
import com.spenvolt.api.model.DeviceOffer;
import com.spenvolt.api.repository.DeviceOfferRepository;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DeviceOfferService {

    private final List<MarketplaceAdapter> adapters;
    private final DeviceOfferRepository repository;

    public List<DeviceOffer> search(String query) {

        List<DeviceOffer> results = new ArrayList<>();

        for (MarketplaceAdapter adapter : adapters) {

            if (!adapter.isAvailable()) {
                continue;
            }

            try {
                List<DeviceOffer> found = adapter.search(query);

                for (DeviceOffer DeviceOffer : found) {

                    // Se já existe exatamente igual, não salva novamente
                    if (exists(DeviceOffer)) {
                        continue;
                    }

                    DeviceOffer saved = repository.save(DeviceOffer);

                    results.add(saved);
                }

            } catch (Exception e) {
                System.err.println("Adapter falhou: " + e.getMessage());
            }
        }

        return results;
    }

    private boolean exists(DeviceOffer DeviceOffer) {

        List<DeviceOffer> devices = repository.findAll();

        for (DeviceOffer existing : devices) {

            if (sameDevice(DeviceOffer, existing)) {
                return true;
            }
        }

        return false;
    }

    private boolean sameDevice(DeviceOffer device1, DeviceOffer device2) {

        try {
            for (Field field : DeviceOffer.class.getDeclaredFields()) {

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