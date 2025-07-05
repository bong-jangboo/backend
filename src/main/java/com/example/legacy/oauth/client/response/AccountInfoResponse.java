package com.example.legacy.oauth.client.response;

import java.util.List;

import com.example.legacy.oauth.client.response.info.AccountInfo;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

public class AccountInfoResponse {
	@JsonProperty("api_tran_id")
	private String apiTranId;

	@JsonProperty("api_tran_dtm")
	private String apiTranDtm;

	@Getter
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
