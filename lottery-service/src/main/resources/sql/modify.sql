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
  `group_sn` int(11) NOT NULL COMMENT '组编号',
  `group_name` varchar(64) NOT NULL COMMENT '组名称',
  `league_type` tinyint(1) default 0 COMMENT '组所属于分类:0足联，1篮联',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='联赛组信息';

-- 联赛国家 --
DROP TABLE IF EXISTS `dl_league_contry`;
CREATE TABLE `dl_league_contry` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `group_id` int(11) NOT NULL COMMENT '所属组id',
  `contry_name` varchar(64) NOT NULL COMMENT '联赛国家名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='联赛国家信息';

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
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COMMENT='联赛信息';


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

















