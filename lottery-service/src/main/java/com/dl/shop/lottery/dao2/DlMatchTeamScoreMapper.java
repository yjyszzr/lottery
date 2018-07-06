package com.dl.shop.lottery.dao2;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlMatchTeamScore;

public interface DlMatchTeamScoreMapper extends Mapper<DlMatchTeamScore> {

	DlMatchTeamScore getByTeamId(@Param("teamId")Integer teamId);
}