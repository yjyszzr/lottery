package com.dl.shop.lottery.model;

import javax.persistence.*;

@Table(name = "dl_match_support")
public class DlMatchSupport {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 竞彩网对应
     */
    @Column(name = "support_id")
    private Integer supportId;

    /**
     * 赛事场次id
     */
    @Column(name = "changci_id")
    private Integer changciId;

    /**
     * 胜支持数
     */
    @Column(name = "win_num")
    private Integer winNum;

    /**
     * 负支持数
     */
    @Column(name = "lose_num")
    private Integer loseNum;

    /**
     * 平支持数
     */
    @Column(name = "draw_num")
    private Integer drawNum;

    /**
     * 支持率
     */
    @Column(name = "pre_win")
    private String preWin;

    /**
     * 支持率
     */
    @Column(name = "pre_lose")
    private String preLose;

    /**
     * 支持率
     */
    @Column(name = "pre_draw")
    private String preDraw;

    /**
     * 总支持数
     */
    private Integer total;

    /**
     * 玩法类型
     */
    @Column(name = "play_type")
    private Integer playType;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Integer createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Integer updateTime;

    /**
     * 拉取平台:0竞彩
     */
    @Column(name = "league_from")
    private Integer leagueFrom;

    /**
     * 获取ID
     *
     * @return id - ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置ID
     *
     * @param id ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取竞彩网对应
     *
     * @return support_id - 竞彩网对应
     */
    public Integer getSupportId() {
        return supportId;
    }

    /**
     * 设置竞彩网对应
     *
     * @param supportId 竞彩网对应
     */
    public void setSupportId(Integer supportId) {
        this.supportId = supportId;
    }

    /**
     * 获取赛事场次id
     *
     * @return changci_id - 赛事场次id
     */
    public Integer getChangciId() {
        return changciId;
    }

    /**
     * 设置赛事场次id
     *
     * @param changciId 赛事场次id
     */
    public void setChangciId(Integer changciId) {
        this.changciId = changciId;
    }

    /**
     * 获取胜支持数
     *
     * @return win_num - 胜支持数
     */
    public Integer getWinNum() {
        return winNum;
    }

    /**
     * 设置胜支持数
     *
     * @param winNum 胜支持数
     */
    public void setWinNum(Integer winNum) {
        this.winNum = winNum;
    }

    /**
     * 获取负支持数
     *
     * @return lose_num - 负支持数
     */
    public Integer getLoseNum() {
        return loseNum;
    }

    /**
     * 设置负支持数
     *
     * @param loseNum 负支持数
     */
    public void setLoseNum(Integer loseNum) {
        this.loseNum = loseNum;
    }

    /**
     * 获取平支持数
     *
     * @return draw_num - 平支持数
     */
    public Integer getDrawNum() {
        return drawNum;
    }

    /**
     * 设置平支持数
     *
     * @param drawNum 平支持数
     */
    public void setDrawNum(Integer drawNum) {
        this.drawNum = drawNum;
    }

    /**
     * 获取支持率
     *
     * @return pre_win - 支持率
     */
    public String getPreWin() {
        return preWin;
    }

    /**
     * 设置支持率
     *
     * @param preWin 支持率
     */
    public void setPreWin(String preWin) {
        this.preWin = preWin;
    }

    /**
     * 获取支持率
     *
     * @return pre_lose - 支持率
     */
    public String getPreLose() {
        return preLose;
    }

    /**
     * 设置支持率
     *
     * @param preLose 支持率
     */
    public void setPreLose(String preLose) {
        this.preLose = preLose;
    }

    /**
     * 获取支持率
     *
     * @return pre_draw - 支持率
     */
    public String getPreDraw() {
        return preDraw;
    }

    /**
     * 设置支持率
     *
     * @param preDraw 支持率
     */
    public void setPreDraw(String preDraw) {
        this.preDraw = preDraw;
    }

    /**
     * 获取总支持数
     *
     * @return total - 总支持数
     */
    public Integer getTotal() {
        return total;
    }

    /**
     * 设置总支持数
     *
     * @param total 总支持数
     */
    public void setTotal(Integer total) {
        this.total = total;
    }

    /**
     * 获取玩法类型
     *
     * @return play_type - 玩法类型
     */
    public Integer getPlayType() {
        return playType;
    }

    /**
     * 设置玩法类型
     *
     * @param playType 玩法类型
     */
    public void setPlayType(Integer playType) {
        this.playType = playType;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Integer getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Integer getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取拉取平台:0竞彩
     *
     * @return league_from - 拉取平台:0竞彩
     */
    public Integer getLeagueFrom() {
        return leagueFrom;
    }

    /**
     * 设置拉取平台:0竞彩
     *
     * @param leagueFrom 拉取平台:0竞彩
     */
    public void setLeagueFrom(Integer leagueFrom) {
        this.leagueFrom = leagueFrom;
    }
}