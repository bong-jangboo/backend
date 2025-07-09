package com.bongjangboo.legacy.oauth.client.response.info;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionInfo {
	@JsonProperty("tran_date")
	private String tranDate;

	@JsonProperty("tran_time")
	private String tranTime;

	@JsonProperty("inout_type")
	private String inoutType;

	@JsonProperty("tran_type")
	private String tranType;

	@JsonProperty("printed_content")
	private String printedContent;

	@JsonProperty("tran_amt")
	private String tranAmt;

	@JsonProperty("after_balance_amt")
	private String afterBalanceAmt;

	@JsonProperty("branch_name")
	private String branchName;
}
