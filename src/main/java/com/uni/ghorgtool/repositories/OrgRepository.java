package com.uni.ghorgtool.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uni.ghorgtool.models.Org;
@Repository
public interface OrgRepository extends JpaRepository<Org, Long> {
    Optional<Org> findByOrgName(String orgLogin);
    @Query("SELECT o FROM Org o JOIN o.users u WHERE u.id = :userId")
    List<Org> findOrgsByUserId(@Param("userId") String userId);
}