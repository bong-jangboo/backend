package com.example.jangboo.oauth.controller.dto.response;

import java.util.List;

import com.example.jangboo.oauth.controller.dto.Info.AccountInfo;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountInfoResponse {
	@JsonProperty("api_tran_id")
	private String apiTranId;

	@JsonProperty("api_tran_dtm")
	private String apiTranDtm;

	@JsonProperty("rsp_code")
	private String rspCode;

	@JsonProperty("rsp_message")
	private String rspMessage;

	@JsonProperty("user_name")
	private String userName;

	@JsonProperty("res_cnt")
	private String resCnt;

	@JsonProperty("res_list")
	private List<AccountInfo> resList;
}
