package com.uni.ghorgtool.services;

import lombok.RequiredArgsConstructor;

import org.springframework.data.util.Pair;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.uni.ghorgtool.dto.OrgCheckResult;
import com.uni.ghorgtool.models.Org;
import com.uni.ghorgtool.models.User;
import com.uni.ghorgtool.repositories.OrgRepository;
import com.uni.ghorgtool.repositories.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final OrgRepository orgRepository;

    public Optional<User> findByUserId(String userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUserById(String id) {
        userRepository.deleteById(id);
    }

    public String getGithubUsername(String githubToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(githubToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "https://api.github.com/user";
        RestTemplate restTemplate = new RestTemplate();

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

    public boolean setOrgs(String email) {
        Optional<User> optUser = userRepository.findByEmail(email);

        if (optUser.isEmpty()) {
            throw new RuntimeException("User with this email doesn't exist");
        }

        User user = optUser.get();

        String githubToken = user.getGithubToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(githubToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        String username = getGithubUsername(githubToken);

        String orgsUrl = "https://api.github.com/user/orgs";
        ResponseEntity<List> response = restTemplate.exchange(
                orgsUrl,
                HttpMethod.GET,
                entity,
                List.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            List<Map<String, Object>> orgs = response.getBody();

            Set<Org> adminOrgs = new HashSet<>();
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

                        Optional<Org> OptOrg = orgRepository.findByOrgName(orgLogin);
                        if (OptOrg != null) {
                            adminOrgs.add(OptOrg.get());
                        }
                    }
                }
            }

            user.setOrgs(adminOrgs);

            return true;
        } else {
            throw new RuntimeException("Failed to fetch organizations from GitHub");
        }
    }

}