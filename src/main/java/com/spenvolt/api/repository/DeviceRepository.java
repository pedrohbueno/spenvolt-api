package com.spenvolt.api.repository;

import com.spenvolt.api.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.EntityGraph;
import java.util.List;


@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer>{
    Device[] findByMembersId(int memberId);
    void deleteByResidenceId(int residenceId);
    @EntityGraph(attributePaths = {"residence", "members"})
    Device[] findByResidenceIdAndResidenceUserId(int residenceId, int userId);
    @EntityGraph(attributePaths = "residence")
    List<Device> findByResidenceUserId(int userId);
}
