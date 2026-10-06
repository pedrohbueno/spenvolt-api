package com.spenvolt.api.adapter;

import com.spenvolt.api.model.Device;

import java.util.List;

public interface DeviceAdapter {
    List<Device> search(String query);
    boolean isAvailable();
}