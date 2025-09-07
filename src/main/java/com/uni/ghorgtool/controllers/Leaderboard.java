package com.uni.ghorgtool.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.uni.ghorgtool.Exception.LeaderboardException;
import com.uni.ghorgtool.dto.request.LeaderboardRefreshRequest;
import com.uni.ghorgtool.dto.request.LeaderboardRequest;
import com.uni.ghorgtool.dto.response.LeaderboardResponse;
import com.uni.ghorgtool.models.Org;
import com.uni.ghorgtool.models.User;
import com.uni.ghorgtool.services.GitHubService;
import com.uni.ghorgtool.services.LeaderboardService;
import com.uni.ghorgtool.services.OrgService;
import com.uni.ghorgtool.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class Leaderboard {

    private final UserService userService;
    private final OrgService orgService;
    private final GitHubService gitHubService;
    private final LeaderboardService leaderboardService;

    @GetMapping("/leaderboard")
    public ResponseEntity<LeaderboardResponse> leaderBoardGet(
            @RequestBody LeaderboardRequest leaderboardRequest,
            Authentication authentication) {

        Long orgId = leaderboardRequest.getOrgId();
        Integer limit = leaderboardRequest.getLimit();
        if (limit == null || limit <= 0)
            limit = 10; // default to 10

        String userEmail = authentication.getName();
        Optional<User> userOpt = userService.findByEmail(userEmail);
        if (userOpt.isEmpty()) {
            throw new LeaderboardException("Unauthorized: User not found.");
        }

        User user = userOpt.get();
        Optional<Org> orgOpt = orgService.findById(orgId);
        if (orgOpt.isEmpty()) {
            throw new LeaderboardException("Organization not found.");
        }

        Org org = orgOpt.get();
        boolean isAdmin = user.getOrgs().stream().anyMatch(o -> o.getId().equals(org.getId()));
        if (!isAdmin) {
            throw new LeaderboardException("Unauthorized: User is not an admin of this organization.");
        }

        List<com.uni.ghorgtool.models.Leaderboard> leaderboard = leaderboardService.getTopContributorsByOrgId(orgId, limit);

        List<LeaderboardResponse.ContributorCommits> contributorList = leaderboard.stream()
                .map(entry -> new LeaderboardResponse.ContributorCommits(entry.getUsername(), entry.getCommits()))
                .collect(Collectors.toList());

        LeaderboardResponse response = new LeaderboardResponse(contributorList);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/leaderboard/refresh")
    public ResponseEntity<LeaderboardResponse> refreshLeaderboard(
            @RequestBody LeaderboardRefreshRequest LeaderboardRefreshRequest, Authentication authentication) {

        Long orgId = LeaderboardRefreshRequest.getOrgId();
        String userEmail = authentication.getName();
        Optional<User> userOpt = userService.findByEmail(userEmail);

        if (userOpt.isEmpty()) {
            throw new LeaderboardException("Unauthorized: User not found.");

        }

        User user = userOpt.get();
        Optional<Org> orgOpt = orgService.findById(orgId);

        if (orgOpt.isEmpty()) {
            throw new LeaderboardException("Organization not found.");

        }

        Org org = orgOpt.get();

        boolean isAdmin = user.getOrgs().stream().anyMatch(o -> o.getId().equals(org.getId()));
        if (!isAdmin) {
            throw new LeaderboardException("Unauthroized: User is not an admin of this organization.");
        }

        Map<String, Integer> contributors = gitHubService.getContributionLeaderboard(org.getOrgName(),
                user.getGithubToken());

        leaderboardService.deleteByOrgId(orgId);

        for (Map.Entry<String, Integer> entry : contributors.entrySet()) {
            String username = entry.getKey();
            Integer commits = entry.getValue();
            com.uni.ghorgtool.models.Leaderboard newLeaderboard = new com.uni.ghorgtool.models.Leaderboard(org,
                    username, commits);
            leaderboardService.saveLeaderboard(newLeaderboard);
        }
        List<LeaderboardResponse.ContributorCommits> topContributors = contributors.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(10)
                .map(e -> new LeaderboardResponse.ContributorCommits(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        LeaderboardResponse response = new LeaderboardResponse(topContributors);
        return ResponseEntity.ok(response);

    }
}
