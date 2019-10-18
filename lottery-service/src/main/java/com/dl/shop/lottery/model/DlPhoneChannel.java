package com.dl.shop.lottery.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "dl_phone_channel")
public class DlPhoneChannel {
	@Id
	@Column(name = "id")
	private Integer id;

	/**
	 * 渠道编码
	 */
	@Column(name = "channel")
	private String channel;
	/**
	 * 渠道名称
	 */
	@Column(name = "channel_name")
	private String channelName;

	/**
	 * app编码
	 */
	@Column(name = "app_code_name")
	private Integer appCodeName;

	/**
	 * 文章Id 多个(逗号隔开)
	 */
	@Column(name = "article_classify_ids")
	private String articleClassifyIds;

	/**
	 * 排序
	 */
	@Column(name = "sorts")
	private String sorts;

	public String getSorts() {
		return sorts;
	}

	public void setSorts(String sorts) {
		this.sorts = sorts;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public Integer getAppCodeName() {
		return appCodeName;
	}

	public void setAppCodeName(Integer appCodeName) {
		this.appCodeName = appCodeName;
	}

	public String getArticleClassifyIds() {
		return articleClassifyIds;
	}

	public void setArticleClassifyIds(String articleClassifyIds) {
		this.articleClassifyIds = articleClassifyIds;
	}

}