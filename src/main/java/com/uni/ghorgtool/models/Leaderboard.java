package com.uni.ghorgtool.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "leaderboard")
public class Leaderboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "org_id")
    private Org org;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private Integer commits;

    // Custom constructor for convenience
    public Leaderboard(Org org, String username, Integer commits) {
        this.org = org;
        this.username = username;
        this.commits = commits;
    }
    // getters and setters
}
