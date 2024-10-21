package com.example.jangboo.role.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.jangboo.role.domain.Role;
import com.example.jangboo.role.domain.RoleRepository;
import com.example.jangboo.role.domain.RoleType;

@Service
public class RoleService {
	@Autowired
	private final RoleRepository roleRepository;

	public RoleService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Transactional
	public void createRole(String role, Long userId) {
		roleRepository.save(
			Role.builder()
				.role(RoleType.valueOf(role))
				.studentId(userId)
				.build()
		);
	}

	@Transactional(readOnly = true)
	public RoleType getCurrentRole(Long studentId) {
		return roleRepository.findByStudentIdAndEndDate(studentId, null).get(0).getRole();
	}

	public String getMainPageUrl(Long userId) {
		String role = getCurrentRole(userId).toString();

		switch (role) {
			case "PRESIDENT":
				return "main_president.html";
			case "VICE_PRESIDENT":
				return "main_vice-president.html";
			case "MANAGER":
				return "main_manager.html";
			case "AUDITOR":
				return "main_auditor.html";
			case "STUDENT":
				return "main_normal.html";
			default:
				return "역할이 없습니다.";
		}
	}
}
