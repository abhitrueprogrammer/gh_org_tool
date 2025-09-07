package com.uni.ghorgtool.services;

import com.uni.ghorgtool.models.Org;
import com.uni.ghorgtool.models.User;
import com.uni.ghorgtool.repositories.OrgRepository;
import com.uni.ghorgtool.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final OrgRepository orgRepository;
    private final GitHubService gitHubService;

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
        return gitHubService.getGithubUsername(githubToken);
    }

    public boolean setOrgs(String email) {
        Optional<User> optUser = userRepository.findByEmail(email);

        if (optUser.isEmpty()) {
            throw new RuntimeException("User with this email doesn't exist");
        }

        User user = optUser.get();

        String githubToken = user.getGithubToken();

        Set<String> adminOrgsNames = gitHubService.getAdminOrgs(githubToken);

        Set<Org> adminOrgs = new HashSet<>();
        for (String orgName : adminOrgsNames) {
            Optional<Org> optOrg = orgRepository.findByOrgName(orgName);
            Org savedOrg = optOrg.orElseGet(() -> orgRepository.save(new Org(orgName)));
            adminOrgs.add(savedOrg);
        }

        user.setOrgs(adminOrgs);
        userRepository.save(user);

        return true;
    }

    public User updateGithubToken(String email, String newGithubToken) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        user.setGithubToken(newGithubToken);
        return userRepository.save(user);
    }

}
