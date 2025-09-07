
-- Create "users" table
CREATE TABLE `users` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `email` TEXT NOT NULL,
  `password` TEXT NOT NULL,
  `github_token` TEXT NOT NULL,
  `role` TEXT NOT NULL,

  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_email` (`email`(255))
);

