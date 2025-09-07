CREATE TABLE orgs (
    org_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    org_name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE user_org (
    user_id BIGINT NOT NULL,
    org_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, org_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (org_id) REFERENCES orgs(org_id)
);
