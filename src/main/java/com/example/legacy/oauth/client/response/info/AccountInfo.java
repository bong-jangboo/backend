package com.example.legacy.oauth.client.response.info;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountInfo {
	@JsonProperty("fintech_use_num")
	private String fintechUseNum;

	@JsonProperty("account_alias")
	private String accountAlias;

	@JsonProperty("bank_code_std")
	private String bankCodeStd;

	@JsonProperty("bank_code_sub")
	private String bankCodeSub;

	@JsonProperty("bank_name")
	private String bankName;

	@JsonProperty("account_num_masked")
	private String accountNumMasked;

	@JsonProperty("account_holder_name")
	private String accountHolderName;

	@JsonProperty("account_holder_type")
	private String accountHolderType;

	@JsonProperty("account_type")
	private String accountType;

	@JsonProperty("inquiry_agree_yn")
	private String inquiryAgreeYn;

	@JsonProperty("inquiry_agree_dtime")
	private String inquiryAgreeDtime;

	@JsonProperty("transfer_agree_yn")
	private String transferAgreeYn;

	@JsonProperty("transfer_agree_dtime")
	private String transferAgreeDtime;

	@JsonProperty("account_state")
	private String accountState;
}
