package com.example.jangboo.transaction.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	Optional<Transaction> findTopByAccountOwnerIdOrderByDateDescTimeDesc(Long accountOwnerId);
	List<Transaction> findTop5ByAccountOwnerIdOrderByDateDescTimeDesc(Long accountOwnerId);
}
