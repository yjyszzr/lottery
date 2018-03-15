package com.dl.shop.lottery.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * 实况
 */
public class JczqShiKuangData {
	public String hostTeam;
	public String guestTeam;
	public String hostRank;
	public String guestRank;
	public List<EventData> eventDataList;
	
	public void addEvent(EventData event) {
		if (eventDataList == null) {
			eventDataList = new LinkedList<JczqShiKuangData.EventData>();
		}
		if (!event.isValid()) {
			return;
		}
		for (EventData eventData : eventDataList) {
			if (eventData.equals(event)) {
				return;
			}
		}
		eventDataList.add(event);
	}
	
	public void addEvent(JczqShiKuangData shiKuangData) {
		if (eventDataList == null) {
			eventDataList = new LinkedList<JczqShiKuangData.EventData>();
		}
		
		for (EventData eventData2 : shiKuangData.eventDataList) {
			for (EventData eventData : eventDataList) {
				if (eventData.equals(eventData2)) {
					continue;
				}
			}
			eventDataList.add(eventData2);
		}
	}
	
	public void sortByTime() {
		if (eventDataList == null) {
			return;
		}
		Collections.sort(eventDataList, new Comparator<EventData>() {
			 @Override
			public int compare(EventData event1, EventData event2) {
				return Integer.valueOf(event1.times).compareTo(Integer.valueOf(event2.times));
			}
		});
	}
	
	public static class EventData {
		/** 主客队事件标志(1：主队，0：客队) */
		public String flag;
		
		/** 事件类型(1、入球 2、红牌  3、黄牌   7、点球  8、乌龙  9、两黄变红)*/
		public String eventtype;
		
		/** 发生时间 */
		public String times;
		
		/** 球员名 */
		public String player_big5;
		
		public boolean isValid() {
			if (!flag.equalsIgnoreCase("0") && !flag.equalsIgnoreCase("1") ) {
				return false;
			}
			try {
				if (Integer.valueOf(times) <= 0) {
					return false;
				}
				if (Integer.valueOf(eventtype) <= 0) {
					return false;
				}
			} catch (Exception e) {
				return false;
			}
			return true;
		}

		@Override
		public boolean equals(Object o) {
			if (o != null && o instanceof EventData) {
				EventData event = (EventData) o;
				if (eventtype.equalsIgnoreCase(event.eventtype) && flag.equalsIgnoreCase(event.flag)
						&& times.equalsIgnoreCase(event.times) && player_big5.equalsIgnoreCase(event.player_big5)) {

					return true;
				}
			}

			return false;
		}

		@Override
		public int hashCode() {
			return super.hashCode();
		}
	}
	
}
