package com.spenvolt.api.repository;

import com.spenvolt.api.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MemberRepository extends JpaRepository<Member, Integer>{
    Member[] findByResidenceId(int residenceId);
}
