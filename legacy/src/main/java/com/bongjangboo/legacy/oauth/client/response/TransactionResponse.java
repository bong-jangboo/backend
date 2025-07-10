package com.bongjangboo.legacy.oauth.client.response;

import java.util.List;

import com.bongjangboo.legacy.oauth.client.response.info.TransactionInfo;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

public class TransactionResponse {
	@JsonProperty("api_tran_id")
	private String apiTranId;

	@JsonProperty("api_tran_dtm")
	private String apiTranDtm;

	@Getter
	@JsonProperty("rsp_code")
	private String rspCode;

	@JsonProperty("rsp_message")
	private String rspMessage;

	@JsonProperty("bank_tran_id")
	private String bankTranId;

	@JsonProperty("bank_tran_date")
	private String bankTranDate;

	@JsonProperty("bank_code_tran")
	private String bankCodeTran;

	@JsonProperty("bank_rsp_code")
	private String bankRspCode;

	@JsonProperty("bank_rsp_message")
	private String bankRspMessage;

	@JsonProperty("bank_name")
	private String bankName;

	@JsonProperty("fintech_use_num")
	private String fintechUseNum;

	@JsonProperty("balance_amt")
	private String balanceAmt;

	@JsonProperty("page_record_cnt")
	private String pageRecordCnt;

	@JsonProperty("next_page_yn")
	private String nextPageYn;

	@JsonProperty("befor_inquiry_trace_info")
	private String beforeInquiryTraceInfo;

	@JsonProperty("res_list")
	private List<TransactionInfo> resList;
}
