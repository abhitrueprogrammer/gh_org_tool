package com.uni.ghorgtool.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uni.ghorgtool.models.Org;
@Repository
public interface OrgRepository extends JpaRepository<Org, String> {
    Optional<Org> findByOrgName(String orgLogin);
}