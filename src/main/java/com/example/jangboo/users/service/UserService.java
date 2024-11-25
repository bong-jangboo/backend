package com.example.jangboo.users.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.jangboo.role.domain.RoleType;
import com.example.jangboo.role.service.RoleService;
import com.example.jangboo.univ.controller.dto.request.RegisterRequest;
import com.example.jangboo.univ.controller.dto.response.UnivInfoResponse;
import com.example.jangboo.univ.service.UnivService;
import com.example.jangboo.users.controller.dto.response.UserInfoResponse;
import com.example.jangboo.users.domain.User;
import com.example.jangboo.users.domain.UserRepository;

@Service
public class UserService {
	@Autowired
	private final UserRepository userRepository;
	@Autowired
	private final PasswordEncoder passwordEncoder;
	@Autowired
	private final UnivService univService;
	@Autowired
	private final RoleService roleService;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UnivService univService,
		 RoleService roleService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.univService = univService;
		this.roleService = roleService;
	}

	@Transactional
	public Long registerUser(RegisterRequest request, Long orgId) {
		return userRepository.save(
			User.builder()
				.name(request.name())
				.number(request.number())
				.deptId(orgId)
				.loginId(request.loginId())
				.password(encodePassword(request.password()))
				.build()
		).getId();
	}

	private String encodePassword(String password) {
		return passwordEncoder.encode(password);
	}

	public UserInfoResponse getUserInfo(Long userId, Long deptId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("유저정보를 가져오지 못했습니다."));
		UnivInfoResponse univInfo = univService.getParentInfo(deptId);
		RoleType role = roleService.getCurrentRole(userId);

		return UserInfoResponse.builder()
			.name(user.getName())
			.dept(univInfo.child())
			.parent(univInfo.parent())
			.role(role.toString())
			.number(user.getNumber())
			.build();
	}

	public Void registerPayedInfo(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("유저정보를 가져오지 못했습니다."));
		user.updatePayedInfo();
		userRepository.save(user);

		return null;
	}

	public Boolean getIsPayed(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("유저정보를 가져오지 못했습니다."));

		return user.getIsPayed();
	}

}
