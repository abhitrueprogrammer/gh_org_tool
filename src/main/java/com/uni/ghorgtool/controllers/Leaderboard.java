package com.uni.ghorgtool.controllers;

import com.uni.ghorgtool.dto.request.LeaderboardRequest;
import com.uni.ghorgtool.models.Org;
import com.uni.ghorgtool.models.User;
import com.uni.ghorgtool.services.GitHubService;
import com.uni.ghorgtool.services.OrgService;
import com.uni.ghorgtool.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class Leaderboard {

    private final UserService userService;
    private final OrgService orgService;
    private final GitHubService gitHubService;

    @PostMapping("/leaderboard/refresh")
    public ResponseEntity<List<Map<String, Object>>> refreshLeaderboard(
            @RequestBody LeaderboardRequest leaderboardRequest, Authentication authentication) {

        Long orgId = leaderboardRequest.getOrgId();
        String userEmail = authentication.getName();
        Optional<User> userOpt = userService.findByEmail(userEmail);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(List.of(Map.of("error", "Unauthorized: User not found.")));
        }

        User user = userOpt.get();
        Optional<Org> orgOpt = orgService.findById(orgId);

        if (orgOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of(Map.of("error", "Organization not found.")));
        }

        Org org = orgOpt.get();

        boolean isAdmin = user.getOrgs().stream().anyMatch(o -> o.getId().equals(org.getId()));
        if (!isAdmin) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(List.of(Map.of("error", "Unauthorized: User is not an admin of this organization.")));
        }

        Map<String, Integer> contributors = gitHubService.getContributionLeaderboard(org.getOrgName(), user.getGithubToken());

        
        List<Map<String, Object>> topContributors = contributors.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(100)
                .map(e -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("login", e.getKey());
                    map.put("contributions", e.getValue());
                    return map;
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(topContributors, HttpStatus.OK);
    }
}
