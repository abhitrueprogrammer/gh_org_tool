package com.uni.ghorgtool.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uni.ghorgtool.models.Leaderboard;

@Repository
public interface LeaderboardRepository extends JpaRepository<Leaderboard, Long> {
    @Query("SELECT l FROM Leaderboard l WHERE l.org.orgId = :orgId ORDER BY l.commits DESC")
    List<Leaderboard> findByOrgOrderByCommitsDesc(@Param("orgId") Long orgId);
}
