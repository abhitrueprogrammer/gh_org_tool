
-- Create "users" table
CREATE TABLE `users` (
  `id` CHAR(36) NOT NULL,
  `email` TEXT NOT NULL,
  `password` TEXT NOT NULL,
  `github_token` TEXT NOT NULL,
  `role` TEXT NOT NULL,

  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_email` (`email`(255))
);

