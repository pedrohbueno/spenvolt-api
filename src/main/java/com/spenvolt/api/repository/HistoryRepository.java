package com.spenvolt.api.repository;

import com.spenvolt.api.model.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<History, Integer> {
    History[] findByResidenceIdOrderByCreatedAtDesc(int residenceId, int userId);

    History[] findByUserIdOrderByCreatedAtDesc(int userId);
    void deleteByResidenceId(int residenceId);

}
