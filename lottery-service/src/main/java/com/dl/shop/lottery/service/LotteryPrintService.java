package com.dl.shop.lottery.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dl.dto.DlQueryAccountDTO;
import com.dl.dto.DlQueryIssueDTO;
import com.dl.dto.DlQueryPrizeFileDTO;
import com.dl.dto.DlQueryStakeDTO;
import com.dl.dto.DlQueryStakeFileDTO;
import com.dl.dto.DlToStakeDTO;
import com.dl.param.DlQueryAccountParam;
import com.dl.param.DlQueryIssueParam;
import com.dl.param.DlQueryPrizeFileParam;
import com.dl.param.DlQueryStakeFileParam;
import com.dl.param.DlQueryStakeParam;
import com.dl.param.DlToStakeParam;

@Service
public class LotteryPrintService {

	@Value("${print.ticket.url}")
	private String printTicketUrl;
	
	/**
	 * 投注接口
	 * @return
	 */
	public DlToStakeDTO toStake(DlToStakeParam param) {
		DlToStakeDTO dlToStakeDTO = new DlToStakeDTO();
		return dlToStakeDTO;
	}
	
	/**
	 * 投注结果查询
	 * @return
	 */
	public DlQueryStakeDTO queryStake(DlQueryStakeParam param) {
		DlQueryStakeDTO dlQueryStakeDTO = new DlQueryStakeDTO();
		return dlQueryStakeDTO;
	}
	
	/**
	 * 期次查询
	 * @return
	 */
	public DlQueryIssueDTO queryIssue(DlQueryIssueParam param) {
		DlQueryIssueDTO dlQueryIssueDTO = new DlQueryIssueDTO();
		return dlQueryIssueDTO;
	}
	
	/**
	 * 账户余额查询
	 * @return
	 */
	public DlQueryAccountDTO queryAccount(DlQueryAccountParam param) {
		DlQueryAccountDTO dlQueryAccountDTO = new DlQueryAccountDTO();
		return dlQueryAccountDTO;
	}
	
	/**
	 * 期次投注对账文件查询
	 * @return
	 */
	public DlQueryStakeFileDTO queryStakeFile(DlQueryStakeFileParam param) {
		DlQueryStakeFileDTO dlQueryStakeFileDTO = new DlQueryStakeFileDTO();
		return dlQueryStakeFileDTO;
	}
	
	/**
	 * 期次中奖文件查询
	 * @return
	 */
	public DlQueryPrizeFileDTO queryPrizeFile(DlQueryPrizeFileParam param) {
		DlQueryPrizeFileDTO dlQueryPrizeFileDTO = new DlQueryPrizeFileDTO();
		return dlQueryPrizeFileDTO;
	}
}
