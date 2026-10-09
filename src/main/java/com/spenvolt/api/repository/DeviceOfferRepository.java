package com.spenvolt.api.repository;

import com.spenvolt.api.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DeviceOfferRepository extends JpaRepository<Device, Long>{
}
