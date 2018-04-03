/************************************************20170909  新增报表功能*************************************************/
DROP TABLE IF EXISTS `dl_league_info`;
CREATE TABLE `dl_league_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `league_id` int(11) NOT NULL COMMENT '联赛编号',
  `league_name` varchar(64) NOT NULL COMMENT '联赛名称',
  `league_addr` varchar(256)  COMMENT '联赛简称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1443248 DEFAULT CHARSET=utf8 COMMENT='联赛信息';
insert into dl_league_info values(1, 78, '德甲', '德甲');
insert into dl_league_info values(2, 79, '意甲', '意甲');
insert into dl_league_info values(3, 81, '法甲', '法甲');
insert into dl_league_info values(4, 127, '英超', '英超');
insert into dl_league_info values(5, 128, '西甲', '西甲');
insert into dl_league_info values(6, 1096, '欧冠', '欧冠');
insert into dl_league_info values(7, 75, '亚冠', '亚冠');
insert into dl_league_info values(8, 489, '巴西杯', '巴西杯');
insert into dl_league_info values(9, 557, '墨西哥杯', '墨西哥杯');




