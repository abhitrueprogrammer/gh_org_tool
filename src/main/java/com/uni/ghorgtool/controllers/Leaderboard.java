package com.uni.ghorgtool.controllers;

import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.uni.ghorgtool.dto.request.LeaderboardRequest;
import com.uni.ghorgtool.models.User;
import com.uni.ghorgtool.services.UserService;

import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

// Rate limiting here
// Add a table containing repos user is an admin of
// Add a table containing leaderboard info. Just create leaderboard once.
@RestController
@RequiredArgsConstructor
public class Leaderboard {
    private final UserService userService;

    @PostMapping("/leaderboard/refresh")
    public ResponseEntity<List<Map<String, Object>>> refreshLeaderboard(
            @RequestParam LeaderboardRequest leaderboardRequest, Authentication authentication) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Integer> contributors = new HashMap<>();
        List<Map<String, Object>> repos = new ArrayList<>();
        String org = leaderboardRequest.getOrg();
        String userEmail = authentication.getName();
        Optional<User> userOpt = userService.findByEmail(userEmail);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        User user = userOpt.get();
        String token = user.getGithubToken();
        HttpHeaders headers = new HttpHeaders();

        headers.set("Accept", "application/vnd.github+json");
        headers.set("Authorization", "token " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        int page = 1;
        while (true) {
            String reposUrl = "https://api.github.com/orgs/" + org + "/repos?per_page=100&page=" + page;
            ResponseEntity<Map[]> response = restTemplate.exchange(reposUrl, HttpMethod.GET, entity, Map[].class);
            Map<String, Object>[] repoArray = response.getBody();
            if (repoArray == null || repoArray.length == 0)
                break;
            repos.addAll(Arrays.asList(repoArray));
            page++;
        }

        for (Map<String, Object> repo : repos) {
            String fullName = (String) repo.get("full_name");
            String contributorsUrl = "https://api.github.com/repos/" + fullName + "/contributors?per_page=100";
            ResponseEntity<Map[]> contribResponse = restTemplate.exchange(contributorsUrl, HttpMethod.GET, entity,
                    Map[].class);
            Map<String, Object>[] contribArray = contribResponse.getBody();
            if (contribArray != null) {
                for (Map<String, Object> c : contribArray) {
                    String login = (String) c.get("login");
                    Integer contribCount = (Integer) c.get("contributions");
                    contributors.put(login, contributors.getOrDefault(login, 0) + contribCount);
                }
            }
        }

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
