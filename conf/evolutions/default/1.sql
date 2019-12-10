# --- !Ups

CREATE TABLE `tasks` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `text` VARCHAR(1000) NOT NULL,
  `is_done` TINYINT NOT NULL,
  `priority` INT NOT NULL,
  PRIMARY KEY (`id`)
);

# --- !Downs

DROP TABLE `tasks`;