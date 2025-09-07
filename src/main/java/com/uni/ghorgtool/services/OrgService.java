package com.uni.ghorgtool.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.uni.ghorgtool.models.Org;
import com.uni.ghorgtool.repositories.OrgRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrgService {
    private final OrgRepository orgRepository;

    public Optional<Org> findById(Long orgId) {
        return orgRepository.findById(orgId);
    }

    public Optional<Org> findByOrgName(String orgName) {
        return orgRepository.findByOrgName(orgName);
    }

    public List<Org> findAll() {
        return orgRepository.findAll();
    }

    public Org saveOrg(Org org) {
        return orgRepository.save(org);
    }

    public void deleteById(Long orgId) {
        orgRepository.deleteById(orgId);
    }

    public List<Org> getOrgsForUser(Long userId) {
        return orgRepository.findOrgsByUserId(userId);
    }
}
