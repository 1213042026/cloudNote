insert into cn_note_status values(2, "delete", "delete");

DROP TABLE IF EXISTS `cn_collection`;
CREATE TABLE `cn_collection` (
  `cn_collection_id` varchar(100) NOT NULL COMMENT '收藏ID',
  `cn_user_id` varchar(100) NOT NULL COMMENT '用户ID',
  `cn_share_id` varchar(100) DEFAULT NULL COMMENT '分享id',
  PRIMARY KEY (`cn_collection_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;