package com.dl.shop.lottery.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "dl_article")
public class DlArticle {
	@Id
	@Column(name = "article_id")
	private Integer articleId;

	/**
	 * 文章标题
	 */
	private String title;

	/**
	 * 文章分类ID
	 */
	@Column(name = "cat_id")
	private Integer catId;

	/**
	 * 关键字
	 */
	private String keywords;

	/**
	 * 阅读权限
	 */
	private String jurisdiction;

	/**
	 * 文章缩略图
	 */
	@Column(name = "article_thumb")
	private String articleThumb;

	/**
	 * 发布时间
	 */
	@Column(name = "add_time")
	private Integer addTime;

	/**
	 * 是否允许评论
	 */
	@Column(name = "is_comment")
	private Integer isComment;

	/**
	 * 点击量
	 */
	@Column(name = "click_number")
	private Integer clickNumber;

	/**
	 * 是否显示
	 */
	@Column(name = "is_show")
	private Integer isShow;

	/**
	 * 发布人ID
	 */
	@Column(name = "user_id")
	private Integer userId;

	/**
	 * 文章状态:1-已发布 2-草稿箱
	 */
	private Integer status;

	/**
	 * 转向连接
	 */
	private String link;

	/**
	 * 文章来源
	 */
	private String source;

	/**
	 * 文章摘要
	 */
	private String summary;

	/**
	 * 分类
	 */
	@Column(name = "extend_cat")
	private String extendCat;

	/**
	 * 是否推荐
	 */
	@Column(name = "is_recommend")
	private Integer isRecommend;

	/**
	 * 作者
	 */
	private String author;

	/**
	 * 比赛id
	 */
	@Column(name = "match_id")
	private Integer matchId;

	/**
	 * 主队- 1 客队 - 2
	 */
	@Column(name = "related_team")
	private String relatedTeam;

	/**
	 * 文章标签
	 */
	@Column(name = "label_defaults")
	private String labelDefaults;

	/**
	 * 列表展示形式 1- 单张图 2-两张图 3-三张图
	 */
	@Column(name = "list_style")
	private Integer listStyle;

	/**
	 * 是否原创 0 - 不是原创 1- 是原创
	 */
	@Column(name = "is_original")
	private Integer isOriginal;

	/**
	 * 删除 1-已删除 0- 未删除
	 */
	@Column(name = "is_delete")
	private Integer isDelete;

	/**
	 * 是否置顶 0 - 未置顶 1-已置顶
	 */
	@Column(name = "is_stick")
	private Integer isStick;

	/**
	 * 置顶时间
	 */
	@Column(name = "stick_time")
	private Integer stickTime;

	/**
	 * 文章的收费价格，暂时没有用到
	 */
	private BigDecimal price;

	/**
	 * 文章等级1,2,3
	 */
	private Integer level;

	@Column(name = "article_pv")
	private Integer articlePv;

	@Column(name = "article_uv")
	private Integer articleUv;

	@Column(name = "video_url")
	private String videoUrl;

	/**
	 * 文章内容
	 */
	private String content;

	/**
	 * @return article_id
	 */
	public Integer getArticleId() {
		return articleId;
	}

	/**
	 * @param articleId
	 */
	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}

	/**
	 * 获取文章标题
	 *
	 * @return title - 文章标题
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 设置文章标题
	 *
	 * @param title
	 *            文章标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 获取文章分类ID
	 *
	 * @return cat_id - 文章分类ID
	 */
	public Integer getCatId() {
		return catId;
	}

	/**
	 * 设置文章分类ID
	 *
	 * @param catId
	 *            文章分类ID
	 */
	public void setCatId(Integer catId) {
		this.catId = catId;
	}

	/**
	 * 获取关键字
	 *
	 * @return keywords - 关键字
	 */
	public String getKeywords() {
		return keywords;
	}

	/**
	 * 设置关键字
	 *
	 * @param keywords
	 *            关键字
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	/**
	 * 获取阅读权限
	 *
	 * @return jurisdiction - 阅读权限
	 */
	public String getJurisdiction() {
		return jurisdiction;
	}

	/**
	 * 设置阅读权限
	 *
	 * @param jurisdiction
	 *            阅读权限
	 */
	public void setJurisdiction(String jurisdiction) {
		this.jurisdiction = jurisdiction;
	}

	/**
	 * 获取文章缩略图
	 *
	 * @return article_thumb - 文章缩略图
	 */
	public String getArticleThumb() {
		return articleThumb;
	}

	/**
	 * 设置文章缩略图
	 *
	 * @param articleThumb
	 *            文章缩略图
	 */
	public void setArticleThumb(String articleThumb) {
		this.articleThumb = articleThumb;
	}

	/**
	 * 获取发布时间
	 *
	 * @return add_time - 发布时间
	 */
	public Integer getAddTime() {
		return addTime;
	}

	/**
	 * 设置发布时间
	 *
	 * @param addTime
	 *            发布时间
	 */
	public void setAddTime(Integer addTime) {
		this.addTime = addTime;
	}

	/**
	 * 获取是否允许评论
	 *
	 * @return is_comment - 是否允许评论
	 */
	public Integer getIsComment() {
		return isComment;
	}

	/**
	 * 设置是否允许评论
	 *
	 * @param isComment
	 *            是否允许评论
	 */
	public void setIsComment(Integer isComment) {
		this.isComment = isComment;
	}

	/**
	 * 获取点击量
	 *
	 * @return click_number - 点击量
	 */
	public Integer getClickNumber() {
		return clickNumber;
	}

	/**
	 * 设置点击量
	 *
	 * @param clickNumber
	 *            点击量
	 */
	public void setClickNumber(Integer clickNumber) {
		this.clickNumber = clickNumber;
	}

	/**
	 * 获取是否显示
	 *
	 * @return is_show - 是否显示
	 */
	public Integer getIsShow() {
		return isShow;
	}

	/**
	 * 设置是否显示
	 *
	 * @param isShow
	 *            是否显示
	 */
	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}

	/**
	 * 获取发布人ID
	 *
	 * @return user_id - 发布人ID
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * 设置发布人ID
	 *
	 * @param userId
	 *            发布人ID
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * 获取文章状态:1-已发布 2-草稿箱
	 *
	 * @return status - 文章状态:1-已发布 2-草稿箱
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * 设置文章状态:1-已发布 2-草稿箱
	 *
	 * @param status
	 *            文章状态:1-已发布 2-草稿箱
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * 获取转向连接
	 *
	 * @return link - 转向连接
	 */
	public String getLink() {
		return link;
	}

	/**
	 * 设置转向连接
	 *
	 * @param link
	 *            转向连接
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * 获取文章来源
	 *
	 * @return source - 文章来源
	 */
	public String getSource() {
		return source;
	}

	/**
	 * 设置文章来源
	 *
	 * @param source
	 *            文章来源
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * 获取文章摘要
	 *
	 * @return summary - 文章摘要
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * 设置文章摘要
	 *
	 * @param summary
	 *            文章摘要
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 * 获取分类
	 *
	 * @return extend_cat - 分类
	 */
	public String getExtendCat() {
		return extendCat;
	}

	/**
	 * 设置分类
	 *
	 * @param extendCat
	 *            分类
	 */
	public void setExtendCat(String extendCat) {
		this.extendCat = extendCat;
	}

	/**
	 * 获取是否推荐
	 *
	 * @return is_recommend - 是否推荐
	 */
	public Integer getIsRecommend() {
		return isRecommend;
	}

	/**
	 * 设置是否推荐
	 *
	 * @param isRecommend
	 *            是否推荐
	 */
	public void setIsRecommend(Integer isRecommend) {
		this.isRecommend = isRecommend;
	}

	/**
	 * 获取作者
	 *
	 * @return author - 作者
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * 设置作者
	 *
	 * @param author
	 *            作者
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * 获取比赛id
	 *
	 * @return match_id - 比赛id
	 */
	public Integer getMatchId() {
		return matchId;
	}

	/**
	 * 设置比赛id
	 *
	 * @param matchId
	 *            比赛id
	 */
	public void setMatchId(Integer matchId) {
		this.matchId = matchId;
	}

	/**
	 * 获取主队- 1 客队 - 2
	 *
	 * @return related_team - 主队- 1 客队 - 2
	 */
	public String getRelatedTeam() {
		return relatedTeam;
	}

	/**
	 * 设置主队- 1 客队 - 2
	 *
	 * @param relatedTeam
	 *            主队- 1 客队 - 2
	 */
	public void setRelatedTeam(String relatedTeam) {
		this.relatedTeam = relatedTeam;
	}

	/**
	 * 获取列表展示形式 1- 单张图 2-两张图 3-三张图
	 *
	 * @return list_style - 列表展示形式 1- 单张图 2-两张图 3-三张图
	 */
	public Integer getListStyle() {
		return listStyle;
	}

	/**
	 * 设置列表展示形式 1- 单张图 2-两张图 3-三张图
	 *
	 * @param listStyle
	 *            列表展示形式 1- 单张图 2-两张图 3-三张图
	 */
	public void setListStyle(Integer listStyle) {
		this.listStyle = listStyle;
	}

	/**
	 * 获取是否原创 0 - 不是原创 1- 是原创
	 *
	 * @return is_original - 是否原创 0 - 不是原创 1- 是原创
	 */
	public Integer getIsOriginal() {
		return isOriginal;
	}

	/**
	 * 设置是否原创 0 - 不是原创 1- 是原创
	 *
	 * @param isOriginal
	 *            是否原创 0 - 不是原创 1- 是原创
	 */
	public void setIsOriginal(Integer isOriginal) {
		this.isOriginal = isOriginal;
	}

	/**
	 * 获取删除 1-已删除 0- 未删除
	 *
	 * @return is_delete - 删除 1-已删除 0- 未删除
	 */
	public Integer getIsDelete() {
		return isDelete;
	}

	/**
	 * 设置删除 1-已删除 0- 未删除
	 *
	 * @param isDelete
	 *            删除 1-已删除 0- 未删除
	 */
	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	/**
	 * 获取是否置顶 0 - 未置顶 1-已置顶
	 *
	 * @return is_stick - 是否置顶 0 - 未置顶 1-已置顶
	 */
	public Integer getIsStick() {
		return isStick;
	}

	/**
	 * 设置是否置顶 0 - 未置顶 1-已置顶
	 *
	 * @param isStick
	 *            是否置顶 0 - 未置顶 1-已置顶
	 */
	public void setIsStick(Integer isStick) {
		this.isStick = isStick;
	}

	/**
	 * 获取置顶时间
	 *
	 * @return stick_time - 置顶时间
	 */
	public Integer getStickTime() {
		return stickTime;
	}

	/**
	 * 设置置顶时间
	 *
	 * @param stickTime
	 *            置顶时间
	 */
	public void setStickTime(Integer stickTime) {
		this.stickTime = stickTime;
	}

	/**
	 * 获取文章的收费价格，暂时没有用到
	 *
	 * @return price - 文章的收费价格，暂时没有用到
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * 设置文章的收费价格，暂时没有用到
	 *
	 * @param price
	 *            文章的收费价格，暂时没有用到
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * 获取文章等级1,2,3
	 *
	 * @return level - 文章等级1,2,3
	 */
	public Integer getLevel() {
		return level;
	}

	/**
	 * 设置文章等级1,2,3
	 *
	 * @param level
	 *            文章等级1,2,3
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}

	/**
	 * @return article_pv
	 */
	public Integer getArticlePv() {
		return articlePv;
	}

	/**
	 * @param articlePv
	 */
	public void setArticlePv(Integer articlePv) {
		this.articlePv = articlePv;
	}

	/**
	 * @return article_uv
	 */
	public Integer getArticleUv() {
		return articleUv;
	}

	/**
	 * @param articleUv
	 */
	public void setArticleUv(Integer articleUv) {
		this.articleUv = articleUv;
	}

	/**
	 * @return video_url
	 */
	public String getVideoUrl() {
		return videoUrl;
	}

	/**
	 * @param videoUrl
	 */
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	/**
	 * 获取文章内容
	 *
	 * @return content - 文章内容
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 设置文章内容
	 *
	 * @param content
	 *            文章内容
	 */
	public void setContent(String content) {
		this.content = content;
	}
}