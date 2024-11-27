package com.example.jangboo.transaction.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.jangboo.accountBook.application.AccountBookService;
import com.example.jangboo.oauth.client.response.MockTransactionResponse;
import com.example.jangboo.receipt.service.ReceiptService;
import com.example.jangboo.receipt.service.dto.response.ReceiptInfoResponse;
import com.example.jangboo.receipt.service.dto.response.ReceiptUrlAndIdResponse;
import com.example.jangboo.transaction.controller.dto.request.TransactionByDateRequest;
import com.example.jangboo.transaction.controller.dto.response.Info.TransactionInfo;
import com.example.jangboo.transaction.controller.dto.response.TransactionDetailResponse;
import com.example.jangboo.transaction.controller.dto.response.TransactionPageResponse;
import com.example.jangboo.transaction.controller.dto.response.TransactionsResponse;
import com.example.jangboo.transaction.domain.Transaction;
import com.example.jangboo.transaction.domain.repository.TransactionRepository;

@Service
public class TransactionService {
	private final TransactionRepository transactionRepository;
	private final AccountBookService accountBookService;

	public static final int PAGE_SIZE = 10;
	private final ReceiptService receiptService;

	public TransactionService(TransactionRepository transactionRepository, AccountBookService accountBookService,
		ReceiptService receiptService) {
		this.transactionRepository = transactionRepository;
		this.accountBookService = accountBookService;
		this.receiptService = receiptService;
	}

	public LocalDateTime findLatestUpdatedTransactionDateTime(Long deptId) {
		return transactionRepository
			.findTopByDeptIdOrderByDateDescTimeDesc(deptId)
			.map(transaction -> LocalDateTime.of(transaction.getDate(), transaction.getTime()))
			.orElseGet(this::getFirstDayOfYear);
	}

	private LocalDateTime getFirstDayOfYear() {
		return LocalDateTime.of(LocalDate.now().withDayOfYear(1), LocalTime.MIDNIGHT); // 1월 1일 00:00:00
	}





	@Transactional
	public void saveTransactions (MockTransactionResponse transaction,Long userId,Long deptId){
		List<Transaction> transactions = transaction.transactions().stream()
			.map(t -> Transaction.builder()
				.transactionType(t.transaction_type())
				.amount(t.amount())
				.date((LocalDate)parseDateTime(t.date(),"yyyy-MM-dd"))
				.time((LocalTime)parseDateTime(t.time(),"HH:mm:ss"))
				.description(t.description())
				.balance(t.balance())
				.lable(t.label())
				.accountOwnerId(userId)
				.deptId(deptId)
				.build()
			)
			.collect(Collectors.toList());

		transactionRepository.saveAll(transactions);
	}

	public Object parseDateTime(String input, String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

		if (pattern.equals("yyyy-MM-dd")) {
			return LocalDate.parse(input, formatter);
		} else if (pattern.equals("HH:mm:ss")) {
			return LocalTime.parse(input, formatter);
		} else if (pattern.equals("yyyy-MM-dd HH:mm:ss")) {
			return LocalDateTime.parse(input, formatter);
		} else {
			throw new IllegalArgumentException("지원하지 않는 포맷입니다.");
		}
	}

	@Transactional(readOnly = true)
	public String getLatestBalance(Long DeptId) {
		return transactionRepository
			.findTopByDeptIdOrderByDateDescTimeDesc(DeptId)
			.orElseThrow(() -> new IllegalStateException("잔액을 가져오지 못했습니다."))
			.getBalance();
	}

	@Transactional(readOnly = true)
	public TransactionsResponse getTop5Transactions(Long accountOwnerId) {
		List<Transaction> transactions = transactionRepository.findTop5ByDeptIdOrderByDateDescTimeDesc(accountOwnerId);

		return new TransactionsResponse(
			transactions.stream().map(TransactionInfo::from).toList()
		);
	}

	@Transactional
	public TransactionPageResponse getTransactionsByDate(String fromDate,String toDate,Long deptId,int pageNo){
		LocalDate parsedFromDate = (LocalDate)parseDateTime(fromDate, "yyyy-MM-dd");
		LocalDate parsedToDate = (LocalDate)parseDateTime(toDate, "yyyy-MM-dd");

		Page<Transaction> transactions = transactionRepository.findByDeptIdAndDateBetween(deptId,parsedFromDate,parsedToDate,getPageable(pageNo,PAGE_SIZE));
		return new TransactionPageResponse(transactions.getContent().stream().map(TransactionInfo::from).toList(),
			transactions.getNumber(),transactions.getTotalPages());
	}

	private Pageable getPageable(int pageNo, int pageSize) {
		return PageRequest.of(pageNo, pageSize);
	}

	@Transactional
	public TransactionsResponse getPayedInfo(String name,Long deptId){
		List<Transaction> transactions = transactionRepository.findByDescriptionContainingAndLableAndDeptIdAndAmount(name,"입금",deptId,"233000.0");
		return new TransactionsResponse(
			transactions.stream().map(TransactionInfo::from).toList()
		);
	}

	@Transactional
	public void updateReceiptId(ReceiptInfoResponse response){
		Transaction transaction = matchTransaction(response);
		transaction.updateReceipt(response.id());
	}

	private Transaction matchTransaction(ReceiptInfoResponse receipt){
		LocalDate localDate = receipt.transactionDate().toLocalDate();
		LocalTime localTime = receipt.transactionDate().toLocalTime();
		String amount = "-"+receipt.amount()+".0";

		Optional<Transaction> transaction = Optional.ofNullable(
			transactionRepository.findByAmountAndDateAndTimeIgnoringSeconds(amount, localDate, localTime)
				.orElseThrow(() -> new IllegalStateException("잔액을 가져오지 못했습니다.")));

		return transaction.get();
	}

	@Transactional(readOnly = true)
	public TransactionPageResponse getNonWriteTransactions(Long deptId, int pageNo) {
		Page<Transaction> transactions = transactionRepository.findByDeptIdAndReceiptIdIsNotNull(deptId, getPageable(pageNo, PAGE_SIZE));

		System.out.println(transactions.getContent().size());
		List<TransactionInfo> filteredTransactions = transactions.getContent().stream()
			.filter(transaction -> accountBookService.isTransactionNotExists(transaction.getId()))
			.map(TransactionInfo::from)
			.toList();

		return new TransactionPageResponse(
			filteredTransactions,
			transactions.getNumber(),
			transactions.getTotalPages()
		);
	}

	@Transactional(readOnly = true)
	public TransactionDetailResponse getDetailTransaction(Long transactionId){
		Transaction transaction = transactionRepository.findById(transactionId).orElse(null);
		ReceiptUrlAndIdResponse receipt = receiptService.getReceiptUrlAndId(transaction.getReceiptId());

		return new TransactionDetailResponse(TransactionInfo.from(transaction),receipt.url(),receipt.id());
	}
}
