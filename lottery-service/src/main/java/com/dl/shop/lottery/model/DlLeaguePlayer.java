package com.dl.shop.lottery.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "dl_league_player")
public class DlLeaguePlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 球员id
     */
    @Column(name = "player_id")
    private Integer playerId;

    /**
     * 球员名称
     */
    @Column(name = "player_name")
    private String playerName;

    /**
     * 球衣号码
     */
    @Column(name = "player_no")
    private Integer playerNo;

    /**
     * 球员位置：0守门员，1后卫，2中场，3前锋
     */
    @Column(name = "player_type")
    private Integer playerType;

    /**
     * 0 竞彩网  1 500万
     */
    @Column(name = "league_from")
    private Integer leagueFrom;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 球队id
     */
    @Column(name = "team_id")
    private Integer teamId;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 国籍
     */
    private String contry;

    /**
     * 球员身价
     */
    @Column(name = "player_price")
    private String playerPrice;

    /**
     * 身高
     */
    private String height;

    /**
     * 体重
     */
    private String weight;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取球员id
     *
     * @return player_id - 球员id
     */
    public Integer getPlayerId() {
        return playerId;
    }

    /**
     * 设置球员id
     *
     * @param playerId 球员id
     */
    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    /**
     * 获取球员名称
     *
     * @return player_name - 球员名称
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * 设置球员名称
     *
     * @param playerName 球员名称
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * 获取球衣号码
     *
     * @return player_no - 球衣号码
     */
    public Integer getPlayerNo() {
        return playerNo;
    }

    /**
     * 设置球衣号码
     *
     * @param playerNo 球衣号码
     */
    public void setPlayerNo(Integer playerNo) {
        this.playerNo = playerNo;
    }

    /**
     * 获取球员位置：0守门员，1后卫，2中场，3前锋
     *
     * @return player_type - 球员位置：0守门员，1后卫，2中场，3前锋
     */
    public Integer getPlayerType() {
        return playerType;
    }

    /**
     * 设置球员位置：0守门员，1后卫，2中场，3前锋
     *
     * @param playerType 球员位置：0守门员，1后卫，2中场，3前锋
     */
    public void setPlayerType(Integer playerType) {
        this.playerType = playerType;
    }

    /**
     * 获取0 竞彩网  1 500万
     *
     * @return league_from - 0 竞彩网  1 500万
     */
    public Integer getLeagueFrom() {
        return leagueFrom;
    }

    /**
     * 设置0 竞彩网  1 500万
     *
     * @param leagueFrom 0 竞彩网  1 500万
     */
    public void setLeagueFrom(Integer leagueFrom) {
        this.leagueFrom = leagueFrom;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取球队id
     *
     * @return team_id - 球队id
     */
    public Integer getTeamId() {
        return teamId;
    }

    /**
     * 设置球队id
     *
     * @param teamId 球队id
     */
    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    /**
     * 获取生日
     *
     * @return birthday - 生日
     */
    public String getBirthday() {
        return birthday;
    }

    /**
     * 设置生日
     *
     * @param birthday 生日
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    /**
     * 获取年龄
     *
     * @return age - 年龄
     */
    public Integer getAge() {
        return age;
    }

    /**
     * 设置年龄
     *
     * @param age 年龄
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * 获取国籍
     *
     * @return contry - 国籍
     */
    public String getContry() {
        return contry;
    }

    /**
     * 设置国籍
     *
     * @param contry 国籍
     */
    public void setContry(String contry) {
        this.contry = contry;
    }

    /**
     * 获取球员身价
     *
     * @return player_price - 球员身价
     */
    public String getPlayerPrice() {
        return playerPrice;
    }

    /**
     * 设置球员身价
     *
     * @param playerPrice 球员身价
     */
    public void setPlayerPrice(String playerPrice) {
        this.playerPrice = playerPrice;
    }

    /**
     * 获取身高
     *
     * @return height - 身高
     */
    public String getHeight() {
        return height;
    }

    /**
     * 设置身高
     *
     * @param height 身高
     */
    public void setHeight(String height) {
        this.height = height;
    }

    /**
     * 获取体重
     *
     * @return weight - 体重
     */
    public String getWeight() {
        return weight;
    }

    /**
     * 设置体重
     *
     * @param weight 体重
     */
    public void setWeight(String weight) {
        this.weight = weight;
    }
}