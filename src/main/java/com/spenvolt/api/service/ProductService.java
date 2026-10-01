package com.spenvolt.api.service;

import com.spenvolt.api.adapter.ProductAdapter;
import com.spenvolt.api.model.Product;
import com.spenvolt.api.repository.ProductRepository;
import jakarta.persistence.Id;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {

    private final List<ProductAdapter> adapters;
    private final ProductRepository repository;

    public ProductService(
            List<ProductAdapter> adapters,
            ProductRepository repository
    ) {
        this.adapters = adapters;
        this.repository = repository;
    }

    public List<Product> search(String query) {

        List<Product> results = new ArrayList<>();

        for (ProductAdapter adapter : adapters) {

            if (!adapter.isAvailable()) {
                continue;
            }

            try {
                List<Product> found = adapter.search(query);

                for (Product product : found) {

                    // Se já existe exatamente igual, não salva novamente
                    if (exists(product)) {
                        continue;
                    }

                    Product saved = repository.save(product);

                    results.add(saved);
                }

            } catch (Exception e) {
                System.err.println("Adapter falhou: " + e.getMessage());
            }
        }

        return repository.findAll();
    }

    private boolean exists(Product product) {

        List<Product> products = repository.findAll();

        for (Product existing : products) {

            if (sameProduct(product, existing)) {
                return true;
            }
        }

        return false;
    }

    private boolean sameProduct(Product product1, Product product2) {

        try {
            for (Field field : Product.class.getDeclaredFields()) {

                // Ignora somente o ID
                if (field.isAnnotationPresent(Id.class)) {
                    continue;
                }

                field.setAccessible(true);

                if (!Objects.equals(
                        field.get(product1),
                        field.get(product2)
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