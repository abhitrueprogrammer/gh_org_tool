package com.uni.ghorgtool.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@NoArgsConstructor
@Entity
@Data
@Table(name = "orgs")
public class Org {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orgId;

    private String orgName;

    @ManyToMany(mappedBy = "orgs")
    private Set<User> users;

    public String getOrgName() {
        return orgName;
    }
    public Long getId() {
        return orgId;
    }
    public Org(String orgName) {
        this.orgName = orgName;
    }

}
