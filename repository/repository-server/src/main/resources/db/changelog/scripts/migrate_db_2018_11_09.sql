DROP TABLE IF EXISTS `user_roles`;
CREATE TABLE `user_roles` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role` varchar(255) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
Insert INTO `user_roles` (`role`, `user_id`)
Select * FROM (
	SELECT 'USER' as role, `user`.id FROM `user` Where `user`.role = 'ADMIN'
	UNION ALL
	SELECT 'MODEL_REVIEWER' as role, `user`.id FROM `user` Where `user`.role = 'ADMIN'
	UNION ALL
    SELECT `user`.role, `user`.id FROM `user`
    UNION ALL
    SELECT 'MODEL_CREATOR' as role, `user`.id FROM `user`
	UNION ALL
	SELECT 'MODEL_PROMOTER' as role, `user`.id FROM `user`
	
    ) as tmp;
ALTER TABLE `user` DROP COLUMN role;
