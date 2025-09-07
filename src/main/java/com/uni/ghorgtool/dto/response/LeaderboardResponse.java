package com.uni.ghorgtool.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LeaderboardResponse {  
    private List<ContributorCommits> contributors;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class ContributorCommits {
        private String name;
        private Integer contributions;
    }
}
