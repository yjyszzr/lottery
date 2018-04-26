package com.dl.lottery.param;

import java.util.List;

import lombok.Data;

@Data
public class QueryMatchResultsByPlayCodesParam {

	private List<String> playCodes;
}
