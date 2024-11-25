package com.example.jangboo.users.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByLoginId(String loginId);
	Optional<User> findByNameAndNumber(String name, String number);
	Page<User> findByIsPayedAndDeptId(boolean isPayed, Long deptId, Pageable pageable);
}
