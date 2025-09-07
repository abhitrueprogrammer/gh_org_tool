-- V3__create_leaderboard_table.sql
CREATE TABLE leaderboard (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    org_id BIGINT NOT NULL,
    username VARCHAR(255) NOT NULL,
    commits INT NOT NULL,
    CONSTRAINT fk_leaderboard_org FOREIGN KEY (org_id) REFERENCES orgs(org_id)
);
