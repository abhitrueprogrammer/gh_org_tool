package com.uni.ghorgtool.models;

import jakarta.persistence.*;

@Entity
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

    // getters and setters
}
