package com.spenvolt.api.repository;

import com.spenvolt.api.model.Residence;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ResidenceRepository extends JpaRepository<Residence, Integer>{
    Residence[] findByUserId(int userId);

    Residence findByIdAndUserId(int id, int userId);
}
