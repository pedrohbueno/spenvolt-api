package com.spenvolt.api.adapter;

import com.spenvolt.api.model.DeviceOffer;

import java.util.List;

public interface MarketplaceAdapter {
    List<DeviceOffer> search(String query);
    boolean isAvailable();
}