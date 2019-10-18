package com.dl.shop.lottery.model;

import javax.persistence.*;

@Table(name = "dl_match_live")
public class DlMatchLive {
    @Id
    @Column(name = "changci_id")
    private Integer changciId;

    @Column(name = "match_live_info")
    private String matchLiveInfo;

    /**
     * @return changci_id
     */
    public Integer getChangciId() {
        return changciId;
    }

    /**
     * @param changciId
     */
    public void setChangciId(Integer changciId) {
        this.changciId = changciId;
    }

    /**
     * @return match_live_info
     */
    public String getMatchLiveInfo() {
        return matchLiveInfo;
    }

    /**
     * @param matchLiveInfo
     */
    public void setMatchLiveInfo(String matchLiveInfo) {
        this.matchLiveInfo = matchLiveInfo;
    }
}