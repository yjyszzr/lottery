package com.dl.lottery.param;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlCallbackStakeSenDeParam implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "彩种", required = true)
	public String lotteryCode;

	@ApiModelProperty(value = "返回结果", required = true)
	public String resultCode;

	@ApiModelProperty(value = "回调类型", required = true)
	public String messageType;

	@ApiModelProperty(value = "返回结果", required = true)
	public String key;

	@ApiModelProperty(value = "内容", required = true)
	public List<SendeResultMessage> message;

	@Data
	public static class SendeResultMessage {

		@ApiModelProperty(value = "返回结果", required = true)
		public String result;

		@ApiModelProperty(value = "出票公司票号", required = true)
		public String orderNumber;

		@ApiModelProperty(value = "出票id", required = true)
		public String ticketId;

		@ApiModelProperty(value = "出票成功时间", required = true)
		public String successTime;

		@ApiModelProperty(value = "出票时赔率", required = true)
		public SpMap odds;

		@Data
		public static class SpMap {
			@ApiModelProperty(value = "", required = true)
			public Odds spMap;

			@Data
			public static class Odds {
				@ApiModelProperty(value = "", required = true)
				public List<MatchNumber> matchNumber;

				@Data
				public static class MatchNumber {
					@ApiModelProperty(value = "", required = true)
					public String matchNumber;
					@ApiModelProperty(value = "", required = true)
					public Map<String, String> value;

				}
			}
		}
	}
}
