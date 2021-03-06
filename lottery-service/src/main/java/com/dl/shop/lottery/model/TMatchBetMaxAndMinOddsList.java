package com.dl.shop.lottery.model;

import java.util.List;
import java.util.Map;

import com.dl.lottery.dto.MatchBetPlayCellDTO;

import lombok.Data;

/**
 * 投注计算临时对象
 * @author 007
 *
 */
@Data
public class TMatchBetMaxAndMinOddsList {

	List<Double> minOddsList;//投注每场最小赔率列表
	List<Double> maxOddsList;//投注每场最小赔率列表
}
