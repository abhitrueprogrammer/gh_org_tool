package com.uni.ghorgtool.models;

import jakarta.persistence.*;
import java.util.Set;

@Entity
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

    

}
