package com.uni.ghorgtool.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.uni.ghorgtool.models.Leaderboard;
import com.uni.ghorgtool.models.Org;
import com.uni.ghorgtool.repositories.LeaderboardRepository;
import com.uni.ghorgtool.repositories.OrgRepository;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LeaderboardService {
    private final LeaderboardRepository leaderboardRepository;

    public Optional<Leaderboard> findById(Long LeaderboardId) {
        return leaderboardRepository.findById(LeaderboardId);
    }

    public List<Leaderboard> findAll() {
        return leaderboardRepository.findAll();
    }

    public Leaderboard saveLeaderboard(Leaderboard Leaderboard) {
        return leaderboardRepository.save(Leaderboard);
    }

    public void deleteById(Long LeaderboardId) {
        leaderboardRepository.deleteById(LeaderboardId);
    }
    @Transactional
    public void deleteByOrgId(Long orgId) {
        leaderboardRepository.deleteByOrg_OrgId(orgId);
    }

}
