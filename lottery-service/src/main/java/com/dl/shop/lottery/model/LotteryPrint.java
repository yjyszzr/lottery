package com.dl.shop.lottery.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

@Table(name = "dl_print_lottery")
public class LotteryPrint {
    /**
     * 出票流水id
     */
    @Id
    @Column(name = "print_lottery_id")
    private Integer printLotteryId;

    /**
     * 订单编号
     */
    @Column(name = "order_sn")
    private String orderSn;
    
    /**
     * 出票单编号
     */
    @Column(name = "ticket_id")
    private String ticketId;

    /**
     * 请求时间
     */
    @Column(name = "accept_time")
    private Integer acceptTime;

    /**
     * 代理商编号
     */
    @Column(name = "merchant")
    private String merchant;

    /**
     * 游戏编号
     */
    @Column(name = "game")
    private String game;

    /**
     * 期次
     */
    @Column(name = "issue")
    private String issue;

    /**
     * 玩法
     */
    @Column(name = "play_type")
    private String playType;

    /**
     * 投注方式
     */
    @Column(name = "bet_type")
    private String betType;

    /**
     * 倍数
     */
    @Column(name = "times")
    private Integer times;

    /**
     * 彩票金额
     */
    @Column(name = "money")
    private BigDecimal money;

    /**
     * 投注号码
     */
    @Column(name = "stakes")
    private String stakes;
    
    /**
     * 中奖号码
     */
    @Column(name = "reward_stakes")
    private String rewardStakes;
    
    /**
     * 出票返回错误码
     */
    @Column(name = "error_code")
    private Integer errorCode;
    
    /**
     * 出票状态 0-出票中 1-已出票
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 中心平台订单编号
     */
    @Column(name = "platform_id")
    private String platformId;
    
    /**
     * 出票返回的状态
     */
    @Column(name = "print_status")
    private Integer printStatus;
    
    /**
     * 出票返回的赔率
     */
    @Column(name = "print_sp")
    private String printSp;
    
    /**
     * 出票返回的票号
     */
    @Column(name = "print_no")
    private String printNo;
    
    /**
     * 返回的出票时间
     */
    @Column(name = "print_time")
    private Date printTime;
    
    /**
     * real_reward_money
     */
    @Column(name = "real_reward_money")
    private BigDecimal realRewardMoney;
    
    
    /**
     * third_part_reward_money
     */
    @Column(name = "third_part_reward_money")
    private BigDecimal thirdPartRewardMoney;
    
    
    /**
     * compare_status
     */
    @Column(name = "compare_status")
    private String compareStatus;
    
    
    /**
     * compared_stakes
     */
    @Column(name = "compared_stakes")
    private String comparedStakes;
    /**
     * print_lottery_com
     */
    @Column(name = "print_lottery_com")
    private Integer printLotteryCom;
    
    public String getComparedStakes() {
		return comparedStakes;
	}

	public void setComparedStakes(String comparedStakes) {
		this.comparedStakes = comparedStakes;
	}

	public BigDecimal getRealRewardMoney() {
		return realRewardMoney;
	}

	public void setRealRewardMoney(BigDecimal realRewardMoney) {
		this.realRewardMoney = realRewardMoney;
	}

	public BigDecimal getThirdPartRewardMoney() {
		return thirdPartRewardMoney;
	}

	public void setThirdPartRewardMoney(BigDecimal thirdPartRewardMoney) {
		this.thirdPartRewardMoney = thirdPartRewardMoney;
	}

	public String getCompareStatus() {
		return compareStatus;
	}

	public void setCompareStatus(String compareStatus) {
		this.compareStatus = compareStatus;
	}

	public String getRewardStakes() {
		return rewardStakes;
	}

	public void setRewardStakes(String rewardStakes) {
		this.rewardStakes = rewardStakes;
	}

	/**
     * 获取出票流水id
     *
     * @return print_lottery_id - 出票流水id
     */
    public Integer getPrintLotteryId() {
        return printLotteryId;
    }

    /**
     * 设置出票流水id
     *
     * @param printLotteryId 出票流水id
     */
    public void setPrintLotteryId(Integer printLotteryId) {
        this.printLotteryId = printLotteryId;
    }

    /**
     * 获取订单编号
     *
     * @return order_sn - 订单编号
     */
    public String getOrderSn() {
        return orderSn;
    }

    /**
     * 设置订单编号
     *
     * @param orderSn 订单编号
     */
    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    /**
     * 获取请求时间
     *
     * @return accept_time - 请求时间
     */
    public Integer getAcceptTime() {
        return acceptTime;
    }

    /**
     * 设置请求时间
     *
     * @param acceptTime 请求时间
     */
    public void setAcceptTime(Integer acceptTime) {
        this.acceptTime = acceptTime;
    }

    /**
     * 获取代理商编号
     *
     * @return merchant - 代理商编号
     */
    public String getMerchant() {
        return merchant;
    }

    /**
     * 设置代理商编号
     *
     * @param merchant 代理商编号
     */
    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    /**
     * 获取游戏编号
     *
     * @return game - 游戏编号
     */
    public String getGame() {
        return game;
    }

    /**
     * 设置游戏编号
     *
     * @param game 游戏编号
     */
    public void setGame(String game) {
        this.game = game;
    }

    /**
     * 获取期次
     *
     * @return issue - 期次
     */
    public String getIssue() {
        return issue;
    }

    /**
     * 设置期次
     *
     * @param issue 期次
     */
    public void setIssue(String issue) {
        this.issue = issue;
    }

    /**
     * 获取玩法
     *
     * @return playType - 玩法
     */
    public String getPlayType() {
        return playType;
    }

    /**
     * 设置玩法
     *
     * @param playtype 玩法
     */
    public void setPlaytype(String playType) {
        this.playType = playType;
    }

    /**
     * 获取投注方式
     *
     * @return betType - 投注方式
     */
    public String getBetType() {
        return betType;
    }

    /**
     * 设置投注方式
     *
     * @param bettype 投注方式
     */
    public void setBettype(String betType) {
        this.betType = betType;
    }

    /**
     * 获取倍数
     *
     * @return times - 倍数
     */
    public Integer getTimes() {
        return times;
    }

    /**
     * 设置倍数
     *
     * @param times 倍数
     */
    public void setTimes(Integer times) {
        this.times = times;
    }

    /**
     * 获取彩票金额
     *
     * @return money - 彩票金额
     */
    public BigDecimal getMoney() {
        return money;
    }

    /**
     * 设置彩票金额
     *
     * @param money 彩票金额
     */
    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    /**
     * 获取投注号码
     *
     * @return stakes - 投注号码
     */
    public String getStakes() {
        return stakes;
    }

    /**
     * 设置投注号码
     *
     * @param stakes 投注号码
     */
    public void setStakes(String stakes) {
        this.stakes = stakes;
    }

    public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getPrintStatus() {
		return printStatus;
	}

	public void setPrintStatus(Integer printStatus) {
		this.printStatus = printStatus;
	}

	public String getPrintSp() {
		return printSp;
	}

	public void setPrintSp(String printSp) {
		this.printSp = printSp;
	}

	public String getPrintNo() {
		return printNo;
	}

	public void setPrintNo(String printNo) {
		this.printNo = printNo;
	}

	public Date getPrintTime() {
		return printTime;
	}

	public void setPrintTime(Date printTime) {
		this.printTime = printTime;
	}

	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}
	
	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	/**
     * 获取中心平台订单编号
     *
     * @return platform_id - 中心平台订单编号
     */
    public String getPlatformId() {
        return platformId;
    }

    /**
     * 设置中心平台订单编号
     *
     * @param platformId 中心平台订单编号
     */
    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

	public Integer getPrintLotteryCom() {
		return printLotteryCom;
	}

	public void setPrintLotteryCom(Integer printLotteryCom) {
		this.printLotteryCom = printLotteryCom;
	}
}