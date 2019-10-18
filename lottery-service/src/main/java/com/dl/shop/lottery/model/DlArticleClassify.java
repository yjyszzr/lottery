package com.dl.shop.lottery.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "dl_article_classify")
public class DlArticleClassify {
	@Id
	@Column(name = "id")
	private Integer id;

	/**
	 * 分类名称
	 */
	@Column(name = "classify_name")
	private String classifyName;

	/**
	 * 是否删除
	 */
	@Column(name = "deleted")
	private Integer deleted;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Integer createTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getClassifyName() {
		return classifyName;
	}

	public void setClassifyName(String classifyName) {
		this.classifyName = classifyName;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

}