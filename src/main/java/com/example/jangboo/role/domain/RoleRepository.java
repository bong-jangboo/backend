package com.example.jangboo.role.domain;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
	List<Role> findByStudentId(Long studentId);

	List<Role> findByStudentIdAndEndDate(Long studentId, Date endDate);

	Optional<Role> findByStudentIdAndRole(Long studentId, RoleType role);

}
