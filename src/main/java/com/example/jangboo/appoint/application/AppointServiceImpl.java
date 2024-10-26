package com.example.jangboo.appoint.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.jangboo.appoint.dto.AppointRequestDto;
import com.example.jangboo.role.domain.Role;
import com.example.jangboo.role.domain.RoleRepository;
import com.example.jangboo.role.domain.RoleType;
import com.example.jangboo.users.domain.User;
import com.example.jangboo.users.domain.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointServiceImpl implements AppointService {

	private final RoleRepository roleRepository;
	private final UserRepository userRepository;

	@Override
	public void appointVicePresidentOrManager(AppointRequestDto requestDto, Long userId) {
		// userId로 현재 사용자의 모든 역할 조회
		List<Role> userRoles = roleRepository.findByStudentId(userId);

		// 현재 사용자의 역할 중 PRESIDENT를 포함하고 있는지 확인
		boolean isPresident = userRoles.stream()
			.anyMatch(role -> role.getRole() == RoleType.PRESIDENT);

		// PRESIDENT 역할이 없으면 에러 발생
		if (!isPresident) {
			throw new IllegalArgumentException("회장만 임명할 수 있는 권한이 있습니다.");
		}

		// User 엔티티에서 이름과 학번으로 userId 조회
		User user = userRepository.findByNameAndNumber(requestDto.getName(), requestDto.getNumber())
			.orElseThrow(() -> new IllegalArgumentException("해당 이름과 학번에 일치하는 사용자가 없습니다."));

		// 입력된 역할에 따라 임명 처리 (부회장 또는 총무)
		RoleType newRoleType;
		if (requestDto.getRole().equalsIgnoreCase("VICE_PRESIDENT")) {
			newRoleType = RoleType.VICE_PRESIDENT;
		} else if (requestDto.getRole().equalsIgnoreCase("MANAGER")) {
			newRoleType = RoleType.MANAGER;
		} else {
			throw new IllegalArgumentException("역할이 유효하지 않습니다. (VICE_PRESIDENT 또는 MANAGER만 가능합니다)");
		}

		// 새로운 역할 저장
		Role newRole = Role.builder()
			.role(newRoleType)
			.studentId(user.getId())  // userId로 저장
			.build();

		roleRepository.save(newRole);
	}
}
