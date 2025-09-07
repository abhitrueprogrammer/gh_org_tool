package com.uni.ghorgtool.dto.github;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Repo {
    @JsonProperty("full_name")
    private String fullName;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
