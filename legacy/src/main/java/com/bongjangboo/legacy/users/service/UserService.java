package com.bongjangboo.legacy.users.service;


import java.util.Optional;

import com.bongjangboo.legacy.users.controller.dto.request.UpdatePayedInfoRequest;
import com.bongjangboo.legacy.users.controller.dto.response.UserInfoResponse;
import com.bongjangboo.legacy.users.controller.dto.response.UserListResponse;
import com.bongjangboo.legacy.users.controller.dto.response.UserSimpleInfo;
import com.bongjangboo.legacy.users.domain.User;
import com.bongjangboo.legacy.users.domain.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bongjangboo.legacy.role.domain.RoleType;
import com.bongjangboo.legacy.role.service.RoleService;
import com.bongjangboo.legacy.univ.controller.dto.request.RegisterRequest;
import com.bongjangboo.legacy.univ.controller.dto.response.UnivInfoResponse;
import com.bongjangboo.legacy.univ.service.UnivService;

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


	@PostConstruct
	public void init() { System.out.println("실행되면 안됨! (현재위치 : UserService)"); }


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

	public Void registerPayedInfo(Long userId, Optional<UpdatePayedInfoRequest> request) {
		// 유저 정보를 가져오는 공통 메서드로 분리
		User user = request
			.map(newRequest -> userRepository.findByNameAndNumber(newRequest.name(), newRequest.number())
				.orElseThrow(() -> new IllegalStateException("유저정보를 가져오지 못했습니다.")))
			.orElseGet(() -> userRepository.findById(userId)
				.orElseThrow(() -> new IllegalStateException("유저정보를 가져오지 못했습니다.")));

		// 공통된 업데이트 로직 실행
		user.updatePayedInfo();
		userRepository.save(user);

		return null;
	}


	public Boolean getIsPayed(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("유저정보를 가져오지 못했습니다."));

		return user.getIsPayed();
	}

	public UserListResponse getPayedUsers(Long deptId, int pageNo, boolean isPayed){
		Page<User> users = userRepository.findByIsPayedAndDeptId(isPayed,deptId,getPageable(pageNo,10));

		return new UserListResponse(
			users.stream().map(UserSimpleInfo::from).toList(),
			users.getNumber(),
			users.getTotalPages()
		);
	}

	private Pageable getPageable(int pageNo, int pageSize) {
		return PageRequest.of(pageNo, pageSize);
	}
}
