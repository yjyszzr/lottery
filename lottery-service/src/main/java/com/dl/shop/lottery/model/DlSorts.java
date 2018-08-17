package com.dl.shop.lottery.model;

public class DlSorts implements Comparable {
	public Integer classifyId;
	public Integer sort;

	public DlSorts(Integer classifyId, Integer sort) {
		this.classifyId = classifyId;
		this.sort = sort;
	}

	public Integer getClassifyId() {
		return classifyId;
	}

	public void setClassifyId(Integer classifyId) {
		this.classifyId = classifyId;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Override
	public int compareTo(Object o) {
		DlSorts s = (DlSorts) o;
		return sort > s.sort ? 1 : (sort == s.sort ? 0 : -1);
	}
}
