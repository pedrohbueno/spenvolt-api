package com.spenvolt.api.adapter;

import com.spenvolt.api.model.Product;

import java.util.List;

public interface ProductAdapter {
    List<Product> search(String query);
    boolean isAvailable();
}