package com.dl.shop.lottery.model;

import javax.persistence.*;

@Table(name = "dl_match_lineups")
public class DlMatchLineUps {
    @Id
    @Column(name = "changci_id")
    private Integer changciId;

    @Column(name = "match_lineups")
    private String matchLineups;

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
     * @return match_lineups
     */
    public String getMatchLineups() {
        return matchLineups;
    }

    /**
     * @param matchLineups
     */
    public void setMatchLineups(String matchLineups) {
        this.matchLineups = matchLineups;
    }
}