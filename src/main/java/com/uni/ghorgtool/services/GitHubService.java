package com.uni.ghorgtool.services;

import com.uni.ghorgtool.dto.github.Contributor;
import com.uni.ghorgtool.dto.github.Repo;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class GitHubService {

    private final RestTemplate restTemplate;

    public GitHubService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Integer> getContributionLeaderboard(String orgName, String githubToken) {
        Map<String, Integer> contributors = new HashMap<>();
        List<Repo> repos = fetchAllRepos(orgName, githubToken);

        for (Repo repo : repos) {
            List<Contributor> repoContributors = fetchContributorsForRepo(repo.getFullName(), githubToken);
            for (Contributor contributor : repoContributors) {
                contributors.merge(contributor.getLogin(), contributor.getContributions(), Integer::sum);
            }
        }

        return contributors;
    }

    public String getGithubUsername(String githubToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(githubToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "https://api.github.com/user";

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return (String) response.getBody().get("login"); // GitHub username
        } else {
            throw new RuntimeException("Failed to fetch GitHub username");
        }
    }

    public Set<String> getAdminOrgs(String githubToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(githubToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String username = getGithubUsername(githubToken);

        String orgsUrl = "https://api.github.com/user/orgs";
        ResponseEntity<List> response = restTemplate.exchange(
                orgsUrl,
                HttpMethod.GET,
                entity,
                List.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            List<Map<String, Object>> orgs = response.getBody();
            Set<String> adminOrgs = new HashSet<>();

            for (Map<String, Object> org : orgs) {
                String orgLogin = (String) org.get("login");
                String membershipUrl = "https://api.github.com/orgs/" + orgLogin + "/memberships/" + username;

                ResponseEntity<Map> membershipResponse = restTemplate.exchange(
                        membershipUrl,
                        HttpMethod.GET,
                        entity,
                        Map.class);

                if (membershipResponse.getStatusCode() == HttpStatus.OK && membershipResponse.getBody() != null) {
                    Map<String, Object> membership = membershipResponse.getBody();
                    String role = (String) membership.get("role");

                    if ("admin".equals(role)) {
                        adminOrgs.add(orgLogin);
                    }
                }
            }
            return adminOrgs;
        } else {
            throw new RuntimeException("Failed to fetch organizations from GitHub");
        }
    }

    private List<Repo> fetchAllRepos(String orgName, String githubToken) {
        List<Repo> allRepos = new ArrayList<>();
        int page = 1;
        while (true) {
            String url = "https://api.github.com/orgs/" + orgName + "/repos?per_page=100&page=" + page;
            HttpHeaders headers = new HttpHeaders();
            if (githubToken != null && !githubToken.isEmpty()) {
                headers.set("Authorization", "token " + githubToken);
            }
            headers.set("Accept", "application/vnd.github+json");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Repo[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Repo[].class);
            if (response.getBody() == null || response.getBody().length == 0) {
                break;
            }
            allRepos.addAll(Arrays.asList(response.getBody()));
            page++;
        }
        return allRepos;
    }

    private List<Contributor> fetchContributorsForRepo(String repoFullName, String githubToken) {
        String url = "https://api.github.com/repos/" + repoFullName + "/contributors?per_page=100";
        HttpHeaders headers = new HttpHeaders();
        if (githubToken != null && !githubToken.isEmpty()) {
            headers.set("Authorization", "token " + githubToken);
        }
        headers.set("Accept", "application/vnd.github+json");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Contributor[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Contributor[].class);
        if (response.getBody() == null) {
            return new ArrayList<>();
        }
        return Arrays.asList(response.getBody());
    }
}
