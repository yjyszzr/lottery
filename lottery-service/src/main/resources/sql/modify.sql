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


-- 2018-04-13 ---

-- 联赛组 --
DROP TABLE IF EXISTS `dl_league_group`;
CREATE TABLE `dl_league_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `group_sn` varchar(8) NOT NULL COMMENT '组编号',
  `group_name` varchar(64) NOT NULL COMMENT '组名称',
  `league_type` tinyint(1) default 0 COMMENT '组所属于分类:0足联，1篮联',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='联赛组信息';
insert into dl_league_group values(1, 'oz', '欧洲赛事', 0);
insert into dl_league_group values(2, 'mz', '美洲赛事', 0);
insert into dl_league_group values(3, 'yz', '亚洲赛事', 0);
insert into dl_league_group values(4, 'gj', '国际赛事', 0);
-- 联赛国家 --
DROP TABLE IF EXISTS `dl_league_contry`;
CREATE TABLE `dl_league_contry` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `group_id` int(11) NOT NULL COMMENT '所属组id',
  `contry_name` varchar(64) NOT NULL COMMENT '联赛国家名称',
  `contry_pic` varchar(256) NOT NULL COMMENT '联赛国家图标',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='联赛国家信息';
insert into dl_league_contry values(1, 1, '英格兰', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_01.jpg');
insert into dl_league_contry values(2, 1, '意大利', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_02.jpg');
insert into dl_league_contry values(3, 1, '西班牙', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_03.jpg');
insert into dl_league_contry values(4, 1, '德国', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_04.jpg');
insert into dl_league_contry values(5, 1, '法国', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_05.jpg');
insert into dl_league_contry values(6, 1, '葡萄牙', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_06.jpg');
insert into dl_league_contry values(7, 1, '荷兰', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_07.jpg');
insert into dl_league_contry values(8, 1, '俄罗斯', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_08.jpg');
insert into dl_league_contry values(9, 1, '苏格兰', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_09.jpg');
insert into dl_league_contry values(10, 1, '挪威', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_10.jpg');
insert into dl_league_contry values(11, 1, '瑞典', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_11.jpg');
insert into dl_league_contry values(12, 1, '比利时', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_12.jpg');
insert into dl_league_contry values(13, 1, '捷克', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_13.jpg');
insert into dl_league_contry values(14, 1, '土耳其', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_14.jpg');
insert into dl_league_contry values(15, 1, '希腊', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_15.jpg');
insert into dl_league_contry values(16, 1, '瑞士', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_16.jpg');
insert into dl_league_contry values(17, 1, '以色列', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_17.jpg');
insert into dl_league_contry values(18, 1, '丹麦', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_18.jpg');
insert into dl_league_contry values(19, 1, '奥地利', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_19.jpg');
insert into dl_league_contry values(20, 1, '波兰', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_20.jpg');
insert into dl_league_contry values(21, 1, '爱尔兰', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_21.jpg');
insert into dl_league_contry values(22, 1, '匈牙利', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_22.jpg');
insert into dl_league_contry values(23, 1, '芬兰', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_23.jpg');
insert into dl_league_contry values(24, 1, '罗马尼亚', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_24.jpg');
insert into dl_league_contry values(25, 1, '塞尔维亚', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_25.jpg');
insert into dl_league_contry values(26, 1, '克罗地亚', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_26.jpg');
insert into dl_league_contry values(27, 1, '乌克兰', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_27.jpg');
insert into dl_league_contry values(28, 1, '冰岛', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_28.jpg');

insert into dl_league_contry values(29, 2, '巴西', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_29.jpg');
insert into dl_league_contry values(30, 2, '阿根廷', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_30.jpg');
insert into dl_league_contry values(31, 2, '智利', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_31.jpg');
insert into dl_league_contry values(32, 2, '美国', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_32.jpg');
insert into dl_league_contry values(33, 2, '墨西哥', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_33.jpg');
insert into dl_league_contry values(34, 3, '中国', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_34.jpg');
insert into dl_league_contry values(35, 3, '日本', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_35.jpg');
insert into dl_league_contry values(36, 3, '韩国', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_36.jpg');
insert into dl_league_contry values(37, 3, '澳大利亚', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_37.jpg');
insert into dl_league_contry values(38, 4, '欧洲赛事', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_38.jpg');
insert into dl_league_contry values(39, 4, '美洲赛事', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_39.jpg');
insert into dl_league_contry values(40, 4, '亚洲赛事', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_40.jpg');
insert into dl_league_contry values(41, 4, '非洲赛事', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_41.jpg');
insert into dl_league_contry values(42, 4, '洲际赛事', 'http://static.sporttery.cn/pres/proj/2016/sporttery-2016/images/league/flag_42.jpg');

-- 联赛信息 --
DROP TABLE IF EXISTS `dl_league_info`;
CREATE TABLE `dl_league_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `league_id` int(11) NOT NULL COMMENT '拉取平台联赛编号',
  `league_name` varchar(64) NOT NULL COMMENT '联赛名称',
  `league_addr` varchar(256) NOT NULL COMMENT '联赛简称',
  `contry_id` int(11) NOT NULL COMMENT '联赛所属国家id',
  `league_type` tinyint(1) default 0 COMMENT '组所属于分类:0足联，1篮联',
  `league_from` tinyint(1) default 0 COMMENT '拉取平台:0竞彩',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=200 DEFAULT CHARSET=utf8 COMMENT='联赛信息';
insert into dl_league_info values(1, 127, '英超', '英超', 1, 0, 0);
insert into dl_league_info values(2, 278, '英冠', '英冠', 1, 0, 0);
insert into dl_league_info values(3, 385, '英甲', '英甲', 1, 0, 0);
insert into dl_league_info values(4, 434, '英乙', '英乙', 1, 0, 0);
insert into dl_league_info values(5, 571, '英足总杯', '英足总杯', 1, 0, 0);
insert into dl_league_info values(6, 585, '英联赛杯', '英联赛杯', 1, 0, 0);
insert into dl_league_info values(7, 604, '英锦标赛', '英锦标赛', 1, 0, 0);
insert into dl_league_info values(8, 631, '社区盾杯', '社区盾杯', 1, 0, 0);

insert into dl_league_info values(9, 79, '意甲', '意甲', 2, 0, 0);
insert into dl_league_info values(10, 249, '意乙', '意乙', 2, 0, 0);
insert into dl_league_info values(11, 734, '意大利杯', '意大利杯', 2, 0, 0);
insert into dl_league_info values(12, 777, '意超杯', '意超杯', 2, 0, 0);

insert into dl_league_info values(13, 128, '西甲', '西甲', 3, 0, 0);
insert into dl_league_info values(14, 248, '西乙', '西乙', 3, 0, 0);
insert into dl_league_info values(15, 555, '国王杯', '国王杯', 3, 0, 0);
insert into dl_league_info values(16, 601, '西超杯', '西超杯', 3, 0, 0);

insert into dl_league_info values(17, 78, '德甲', '德甲', 4, 0, 0);
insert into dl_league_info values(18, 247, '德乙', '德乙', 4, 0, 0);
insert into dl_league_info values(19, 733, '德国杯', '德国杯', 4, 0, 0);
insert into dl_league_info values(20, 800, '德超杯', '德超杯', 4, 0, 0);

insert into dl_league_info values(21, 81, '法甲', '法甲', 5, 0, 0);
insert into dl_league_info values(22, 250, '法乙', '法乙', 5, 0, 0);
insert into dl_league_info values(23, 772, '法联赛杯', '法联赛杯', 5, 0, 0);
insert into dl_league_info values(24, 754, '法国杯', '法国杯', 5, 0, 0);
insert into dl_league_info values(25, 797, '法超杯', '法超杯', 5, 0, 0);

insert into dl_league_info values(26, 140, '葡超', '葡超', 6, 0, 0);
insert into dl_league_info values(27, 291, '葡超', '葡超', 6, 0, 0);
insert into dl_league_info values(28, 570, '葡萄牙杯', '葡萄牙杯', 6, 0, 0);
insert into dl_league_info values(29, 586, '葡萄赛杯', '葡萄赛杯', 6, 0, 0);
insert into dl_league_info values(30, 600, '葡超杯', '葡超杯', 6, 0, 0);

insert into dl_league_info values(31, 82, '荷甲', '荷甲', 7, 0, 0);
insert into dl_league_info values(32, 246, '荷乙', '荷乙', 7, 0, 0);
insert into dl_league_info values(33, 569, '荷兰杯', '荷兰杯', 7, 0, 0);
insert into dl_league_info values(34, 587, '荷超杯', '荷超杯', 7, 0, 0);

insert into dl_league_info values(35, 151, '俄超', '俄超', 8, 0, 0);
insert into dl_league_info values(36, 294, '俄甲', '俄甲', 8, 0, 0);
insert into dl_league_info values(37, 553, '俄罗斯杯', '俄罗斯杯', 8, 0, 0);
insert into dl_league_info values(38, 588, '俄超杯', '俄超杯', 8, 0, 0);

insert into dl_league_info values(39, 137, '苏超', '苏超', 9, 0, 0);
insert into dl_league_info values(40, 286, '苏冠', '苏冠', 9, 0, 0);
insert into dl_league_info values(41, 689, '苏联赛杯', '苏联赛杯', 9, 0, 0);
insert into dl_league_info values(42, 676, '苏足总杯', '苏足总杯', 9, 0, 0);

insert into dl_league_info values(43, 133, '挪超', '挪超', 10, 0, 0);
insert into dl_league_info values(44, 282, '挪甲', '挪甲', 10, 0, 0);
insert into dl_league_info values(45, 741, '挪威杯', '挪威杯', 10, 0, 0);
insert into dl_league_info values(46, 1201, '挪超杯', '挪超杯', 10, 0, 0);

insert into dl_league_info values(47, 132, '瑞超', '瑞超', 11, 0, 0);
insert into dl_league_info values(48, 283, '瑞甲', '瑞甲', 11, 0, 0);
insert into dl_league_info values(49, 566, '瑞典杯', '瑞典杯', 11, 0, 0);
insert into dl_league_info values(50, 590, '瑞超杯', '瑞超杯', 11, 0, 0);

insert into dl_league_info values(51, 83, '比甲', '比甲', 12, 0, 0);
insert into dl_league_info values(52, 252, '比乙', '比乙', 12, 0, 0);
insert into dl_league_info values(53, 563, '比利时杯', '比利时杯', 12, 0, 0);
insert into dl_league_info values(54, 572, '比超杯', '比超杯', 12, 0, 0);

insert into dl_league_info values(55, 95, '捷甲', '捷甲', 13, 0, 0);
insert into dl_league_info values(56, 255, '捷乙', '捷乙', 13, 0, 0);
insert into dl_league_info values(57, 736, '捷克杯', '捷克杯', 13, 0, 0);
insert into dl_league_info values(58, 804, '捷超杯', '捷超杯', 13, 0, 0);


insert into dl_league_info values(59, 129, '土超', '土超', 14, 0, 0);
insert into dl_league_info values(60, 289, '土甲', '土甲', 14, 0, 0);
insert into dl_league_info values(61, 486, '土耳其杯', '土耳其杯', 14, 0, 0);
insert into dl_league_info values(62, 515, '土超杯', '土超杯', 14, 0, 0);

insert into dl_league_info values(63, 148, '希超', '希超', 15, 0, 0);
insert into dl_league_info values(64, 292, '希甲', '希甲', 15, 0, 0);
insert into dl_league_info values(65, 738, '希腊杯', '希腊杯', 15, 0, 0);

insert into dl_league_info values(66, 131, '瑞士超', '瑞士超', 16, 0, 0);
insert into dl_league_info values(67, 290, '瑞士甲', '瑞士甲', 16, 0, 0);
insert into dl_league_info values(68, 567, '瑞士杯', '瑞士杯', 16, 0, 0);

insert into dl_league_info values(69, 149, '以超', '以超', 17, 0, 0);
insert into dl_league_info values(70, 296, '以甲', '以甲', 17, 0, 0);
insert into dl_league_info values(71, 722, '以色列杯', '以色列杯', 17, 0, 0);

insert into dl_league_info values(72, 134, '丹超', '丹超', 18, 0, 0);
insert into dl_league_info values(73, 285, '丹甲', '丹甲', 18, 0, 0);
insert into dl_league_info values(74, 740, '丹麦杯', '丹麦杯', 18, 0, 0);

insert into dl_league_info values(75, 85, '奥甲', '奥甲', 19, 0, 0);
insert into dl_league_info values(76, 251, '奥乙', '奥乙', 19, 0, 0);
insert into dl_league_info values(77, 729, '奥地利杯', '奥地利杯', 19, 0, 0);

insert into dl_league_info values(78, 150, '波甲', '波甲', 20, 0, 0);
insert into dl_league_info values(79, 293, '波乙', '波乙', 20, 0, 0);
insert into dl_league_info values(80, 564, '波兰杯', '波兰杯', 20, 0, 0);
insert into dl_league_info values(81, 603, '波超杯', '波超杯', 20, 0, 0);

insert into dl_league_info values(82, 136, '爱超', '爱超', 21, 0, 0);
insert into dl_league_info values(83, 288, '爱甲', '爱甲', 21, 0, 0);
insert into dl_league_info values(84, 774, '爱联赛杯', '爱联赛杯', 21, 0, 0);
insert into dl_league_info values(85, 757, '爱足总杯', '爱足总杯', 21, 0, 0);

insert into dl_league_info values(86, 89, '匈甲', '匈甲', 22, 0, 0);
insert into dl_league_info values(87, 287, '匈乙', '匈乙', 22, 0, 0);
insert into dl_league_info values(88, 726, '匈牙利杯', '匈牙利杯', 22, 0, 0);
insert into dl_league_info values(89, 776, '匈联赛杯', '匈联赛杯', 22, 0, 0);
insert into dl_league_info values(90, 796, '匈超杯', '匈超杯', 22, 0, 0);

insert into dl_league_info values(91, 130, '芬超', '芬超', 23, 0, 0);
insert into dl_league_info values(92, 281, '芬甲', '芬甲', 23, 0, 0);
insert into dl_league_info values(93, 773, '芬联赛杯', '芬联赛杯', 23, 0, 0);
insert into dl_league_info values(94, 739, '芬兰杯', '芬兰杯', 23, 0, 0);

insert into dl_league_info values(95, 96, '罗甲', '罗甲', 24, 0, 0);
insert into dl_league_info values(96, 261, '罗乙', '罗乙', 24, 0, 0);
insert into dl_league_info values(97, 444, '罗杯', '罗杯', 24, 0, 0);
insert into dl_league_info values(98, 491, '罗超杯', '罗超杯', 24, 0, 0);

insert into dl_league_info values(99, 157, '塞超', '塞超', 25, 0, 0);
insert into dl_league_info values(100, 304, '塞甲', '塞甲', 25, 0, 0);
insert into dl_league_info values(101, 562, '塞杯', '塞杯', 25, 0, 0);

insert into dl_league_info values(102, 87, '克甲', '克甲', 26, 0, 0);
insert into dl_league_info values(103, 254, '克乙', '克乙', 26, 0, 0);
insert into dl_league_info values(104, 723, '克杯', '克杯', 26, 0, 0);

insert into dl_league_info values(105, 153, '乌超', '乌超', 27, 0, 0);
insert into dl_league_info values(106, 298, '乌甲', '乌甲', 27, 0, 0);
insert into dl_league_info values(107, 450, '乌克兰杯', '乌克兰杯', 27, 0, 0);
insert into dl_league_info values(108, 514, '乌超杯', '乌超杯', 27, 0, 0);

insert into dl_league_info values(109, 135, '冰超', '冰超', 28, 0, 0);
insert into dl_league_info values(110, 284, '冰甲', '冰甲', 28, 0, 0);
insert into dl_league_info values(111, 724, '冰岛杯', '冰岛杯', 28, 0, 0);
insert into dl_league_info values(112, 786, '冰超杯', '冰超杯', 28, 0, 0);

insert into dl_league_info values(113, 84, '巴甲', '巴甲', 29, 0, 0);
insert into dl_league_info values(114, 257, '巴乙', '巴乙', 29, 0, 0);
insert into dl_league_info values(115, 556, '圣保罗锦', '圣保罗锦', 29, 0, 0);
insert into dl_league_info values(116, 489, '巴西杯', '巴西杯', 29, 0, 0);

insert into dl_league_info values(117, 98, '阿甲', '阿甲', 30, 0, 0);
insert into dl_league_info values(118, 256, '阿乙', '阿乙', 30, 0, 0);
insert into dl_league_info values(119, 864, '阿根廷杯', '阿根廷杯', 30, 0, 0);
insert into dl_league_info values(120, 875, '阿超杯', '阿超杯', 30, 0, 0);

insert into dl_league_info values(121, 99, '智利甲', '智利甲', 31, 0, 0);
insert into dl_league_info values(122, 529, '智利杯', '智利杯', 31, 0, 0);
insert into dl_league_info values(123, 533, '智超杯', '智超杯', 31, 0, 0);

insert into dl_league_info values(124, 124, '美职足', '美职足', 32, 0, 0);
insert into dl_league_info values(125, 568, '公开赛杯', '公开赛杯', 32, 0, 0);

insert into dl_league_info values(126, 109, '墨超', '墨超', 33, 0, 0);
insert into dl_league_info values(127, 557, '墨西哥杯', '墨西哥杯', 33, 0, 0);
insert into dl_league_info values(128, 581, '墨超杯', '墨超杯', 33, 0, 0);
insert into dl_league_info values(129, 1262, '墨冠杯', '墨冠杯', 33, 0, 0);

insert into dl_league_info values(130, 139, '中超', '中超', 34, 0, 0);
insert into dl_league_info values(131, 212, '中甲', '中甲', 34, 0, 0);
insert into dl_league_info values(132, 322, '中足杯', '中足杯', 34, 0, 0);
insert into dl_league_info values(133, 409, '中超杯', '中超杯', 34, 0, 0);

insert into dl_league_info values(134, 50, '日职', '日职', 35, 0, 0);
insert into dl_league_info values(135, 241, '日乙', '日乙', 35, 0, 0);
insert into dl_league_info values(136, 440, '天皇杯', '天皇杯', 35, 0, 0);
insert into dl_league_info values(137, 392, '日联赛杯', '日联赛杯', 35, 0, 0);
insert into dl_league_info values(138, 451, '日超杯', '日超杯', 35, 0, 0);

insert into dl_league_info values(139, 51, '韩职', '韩职', 36, 0, 0);
insert into dl_league_info values(140, 230, '韩挑战联', '韩挑战联', 36, 0, 0);
insert into dl_league_info values(141, 452, '韩足总杯', '韩足总杯', 36, 0, 0);

insert into dl_league_info values(142, 39, '澳超', '澳超', 37, 0, 0);
insert into dl_league_info values(143, 229, '澳杯', '澳杯', 37, 0, 0);

insert into dl_league_info values(144, 1083, '欧洲杯', '欧洲杯', 38, 0, 0);
insert into dl_league_info values(145, 1096, '欧冠', '欧冠', 38, 0, 0);
insert into dl_league_info values(146, 1092, '世欧预', '世欧预', 38, 0, 0);
insert into dl_league_info values(147, 1091, '欧预赛', '欧预赛', 38, 0, 0);
insert into dl_league_info values(148, 1103, '欧罗巴', '欧罗巴', 38, 0, 0);
insert into dl_league_info values(149, 1114, '欧超杯', '欧超杯', 38, 0, 0);
insert into dl_league_info values(150, 1161, '欧青赛', '欧青赛', 38, 0, 0);
insert into dl_league_info values(151, 1167, '女欧洲杯', '女欧洲杯', 38, 0, 0);
insert into dl_league_info values(152, 1170, '女欧预', '女欧预', 38, 0, 0);

insert into dl_league_info values(153, 1137, '美洲杯', '美洲杯', 39, 0, 0);
insert into dl_league_info values(154, 1118, '解放者杯', '解放者杯', 39, 0, 0);
insert into dl_league_info values(155, 1094, '世南美预', '世南美预', 39, 0, 0);
insert into dl_league_info values(156, 1142, '俱乐部杯', '俱乐部杯', 39, 0, 0);
insert into dl_league_info values(157, 1149, '优胜者杯', '优胜者杯', 39, 0, 0);
insert into dl_league_info values(158, 353, '世中北美预', '世中北美预', 39, 0, 0);
insert into dl_league_info values(159, 31, '中美洲杯', '中美洲杯', 39, 0, 0);
insert into dl_league_info values(160, 313, '中北美冠', '中北美冠', 39, 0, 0);
insert into dl_league_info values(161, 28, '金杯赛', '金杯赛', 39, 0, 0);

insert into dl_league_info values(162, 29, '亚洲杯', '亚洲杯', 40, 0, 0);
insert into dl_league_info values(163, 75, '亚冠', '亚冠', 40, 0, 0);
insert into dl_league_info values(164, 34, '世亚预', '世亚预', 40, 0, 0);
insert into dl_league_info values(165, 412, '亚预赛', '亚预赛', 40, 0, 0);
insert into dl_league_info values(166, 238, '四强赛', '四强赛', 40, 0, 0);
insert into dl_league_info values(167, 1209, '亚青赛', '亚青赛', 40, 0, 0);
insert into dl_league_info values(168, 76, '亚运男足', '亚运男足', 40, 0, 0);
insert into dl_league_info values(169, 848, '亚运女足', '亚运女足', 40, 0, 0);
insert into dl_league_info values(170, 312, '西亚锦', '西亚锦', 40, 0, 0);
insert into dl_league_info values(171, 237, '东南亚锦', '东南亚锦', 40, 0, 0);
insert into dl_league_info values(172, 845, '女亚洲杯', '女亚洲杯', 40, 0, 0);
insert into dl_league_info values(173, 856, '女四强赛', '女四强赛', 40, 0, 0);

insert into dl_league_info values(174, 164, '非洲杯', '非洲杯', 41, 0, 0);
insert into dl_league_info values(175, 245, '世非预', '世非预', 41, 0, 0);
insert into dl_league_info values(176, 503, '非预赛', '非预赛', 41, 0, 0);
insert into dl_league_info values(177, 530, '非国锦', '非国锦', 41, 0, 0);

insert into dl_league_info values(178, 1076, '世界杯', '世界杯', 42, 0, 0);
insert into dl_league_info values(179, 1213, '女世界杯', '女世界杯', 42, 0, 0);
insert into dl_league_info values(180, 1176, '奥运男足', '奥运男足', 42, 0, 0);
insert into dl_league_info values(181, 1222, '奥运女足', '奥运女足', 42, 0, 0);
insert into dl_league_info values(182, 538, '联合会杯', '联合会杯', 42, 0, 0);
insert into dl_league_info values(183, 1129, '世预附加', '世预附加', 42, 0, 0);
insert into dl_league_info values(184, 1189, '世青赛', '世青赛', 42, 0, 0);
insert into dl_league_info values(185, 711, '世俱杯', '世俱杯', 42, 0, 0);
insert into dl_league_info values(186, 1207, '国冠杯', '国冠杯', 42, 0, 0);
insert into dl_league_info values(187, 1306, '中国杯', '中国杯', 42, 0, 0);

-- 球队 --
DROP TABLE IF EXISTS `dl_league_team`;
CREATE TABLE `dl_league_team` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `team_id` int(11) NOT NULL COMMENT '球队编号',
  `team_name` varchar(64) NOT NULL COMMENT '球队名称',
  `team_addr` varchar(64) NOT NULL COMMENT '球队简称',
  `team_pic` varchar(256) NOT NULL COMMENT '球队图标',
  `league_from` tinyint(1) default 0 COMMENT '拉取平台:0竞彩',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COMMENT='球队信息';

-- 球队联赛关系 --
DROP TABLE IF EXISTS `dl_league_team_ref`;
CREATE TABLE `dl_league_team_ref` (
  `team_id` int(11) NOT NULL COMMENT '球队编号',
  `league_id` int(11) NOT NULL COMMENT '联赛编号',
  
  `league_from` tinyint(1) default 0 COMMENT '拉取平台:0竞彩',
  PRIMARY KEY (`team_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COMMENT='球队联赛关系';

-- 赛事信息 --
DROP TABLE IF EXISTS `dl_league_match`;
CREATE TABLE `dl_league_match` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `match_id` int(11) NOT NULL COMMENT '赛事id',
  `match_time` timestamp NOT NULL COMMENT '开赛时间',
  `match_sn` varchar(16) NOT NULL COMMENT '赛事编号:周一001',
  `show_time` date NOT NULL COMMENT '赛事展示分组',
  `play_code` varchar(16) NOT NULL COMMENT '赛事编码',
  `league_id` int(11) NOT NULL COMMENT '联赛id',
  `home_team_id` int(11) NOT NULL COMMENT '主队id',
  `home_team_name` varchar(64) NOT NULL COMMENT '主队名称',
  `visiting_team_id` int(11) NOT NULL COMMENT '负队id',
  `visiting_team_name` varchar(64) NOT NULL COMMENT '负队名称',
  `is_hot` tinyint(1) NOT NULL default 0 COMMENT '是否热门，0非，1是',
  `half_rst` varchar(16) COMMENT '半场比分(结果)',
  `final_rst` varchar(16)  COMMENT '全场比分(结果)',
  `h_odds` double(6,2)  COMMENT '胜赔率(结果)',
  `d_odds` double(6,2)  COMMENT '平赔率(结果)',
  `a_odds` double(6,2)  COMMENT '负赔率(结果)',
  `status` tinyint(1) default 0 COMMENT '是否拉取结果:0未1完成',
  `is_del` tinyint(1) default 0 COMMENT '是否删除:0否1是',
  `is_show` tinyint(1) default 1 COMMENT '是否展示:0否1是',
  `create_time` int(11) NOT NULL COMMENT '创建时间',
  `update_time` int(11) NOT NULL COMMENT '更新时间',
  
 `league_from` tinyint(1) default 0 COMMENT '拉取平台:0竞彩',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COMMENT='赛事信息';


-- 赛事玩法信息 --
DROP TABLE IF EXISTS `dl_league_match_play`;
CREATE TABLE `dl_league_match_play` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `match_id` int(11) NOT NULL COMMENT '赛事id',
  `play_type` tinyint(1) NOT NULL COMMENT '玩法类型',
  `play_content` varchar(16) NOT NULL COMMENT '赛事编号:周一001',
  `create_time` int(11) NOT NULL COMMENT '创建时间',
  
  `league_from` tinyint(1) default 0 COMMENT '拉取平台:0竞彩',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COMMENT='赛事信息';

-- 赛事结果 ---
DROP TABLE IF EXISTS `dl_league_match_result`;
CREATE TABLE `dl_league_match_result` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `changci_id` int(11) NOT NULL COMMENT '赛事场次id',
  `play_type` tinyint(1) NOT NULL COMMENT '玩法',
  `play_code` varchar(16) NOT NULL COMMENT '赛事编码',
  `cell_code` varchar(16) NOT NULL COMMENT '结果编码',
  `cell_name` varchar(16) NOT NULL COMMENT '结果名称',
  `goalline` varchar(8)  COMMENT '让球',
  `single` tinyint(1) default 0 COMMENT '单关：0非单关，1单关',
  `odds` double(6,2) NOT NULL COMMENT '赔率',
  `create_time` int(11) NOT NULL COMMENT '创建时间',
  
  `league_from` tinyint(1) default 0 COMMENT '拉取平台:0竞彩',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COMMENT='赛事结果';


-- 亚盘数据 ---
DROP TABLE IF EXISTS `dl_league_match_asia`;
CREATE TABLE `dl_league_match_asia` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `changci_id` int(11) NOT NULL COMMENT '赛事场次id',
  `asia_id` int(11) NOT NULL COMMENT '亚盘对应竞彩网id',
  `com_name` varchar(16) NOT NULL COMMENT '公司名称',
  `init_odds1` double(6,2) NOT NULL COMMENT '初始水位',
  `init_rule` varchar(16) NOT NULL COMMENT '初始盘口',
  `init_odds2` double(6,2) NOT NULL COMMENT '初始水位赔率',
  `real_odds1` double(6,2) NOT NULL COMMENT '即时水位',
  `real_rule` varchar(16) NOT NULL COMMENT '即时盘口',
  `real_odds2` double(6,2) NOT NULL COMMENT '即时水位赔率',
  `odds1_change` tinyint(1) default 0 COMMENT '变化趋势:0equal,1up,2down',
  `odds2_change` tinyint(1) default 0 COMMENT '变化趋势:0equal,1up,2down',
  `time_minus` int(11) NOT NULL COMMENT '最新更新时间，以分为单位，如：大于一小时展示用',
  `ratio_h` double(6,2) NOT NULL COMMENT '最新概率主',
  `ratio_a` double(6,2) NOT NULL COMMENT '最新概率客',
  `index_h` double(6,2) NOT NULL COMMENT '凯利指数主',
  `index_a` double(6,2) NOT NULL COMMENT '凯利指数客',
  `create_time` int(11) NOT NULL COMMENT '创建时间',
  `update_time` int(11) NOT NULL COMMENT '更新时间',
  
  `league_from` tinyint(1) default 0 COMMENT '拉取平台:0竞彩',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COMMENT='赛事结果';

















