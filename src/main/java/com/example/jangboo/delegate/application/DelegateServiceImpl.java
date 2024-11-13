package com.example.jangboo.delegate.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.jangboo.delegate.dto.DelegateRequestDto;
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
public class DelegateServiceImpl implements DelegateService {

	private final RoleRepository roleRepository;
	private final UserRepository userRepository;

	@Override
	public void mandateRole(DelegateRequestDto requestDto, Long info) {
		List<Role> currentUserRoles = roleRepository.findByStudentId(info);

		// 현재 사용자가 PRESIDENT, VICE_PRESIDENT, AUDITOR 중 하나의 역할을 갖고 있는지 확인
		boolean hasRequiredRole = currentUserRoles.stream()
			.anyMatch(role -> role.getRole() == RoleType.PRESIDENT
				|| role.getRole() == RoleType.VICE_PRESIDENT
				|| role.getRole() == RoleType.MANAGER);

		if (!hasRequiredRole) {
			throw new IllegalArgumentException("해당 역할을 위임할 권한이 없습니다.");
		}

		// 이름과 학번으로 위임받을 유저 찾기
		User delegateUser = userRepository.findByNameAndNumber(requestDto.getName(), requestDto.getNumber())
			.orElseThrow(() -> new IllegalArgumentException("해당 이름과 학번에 일치하는 사용자가 없습니다."));

		// 현재 사용자의 역할 중에서 위임 가능한 역할(PRESIDENT, VICE_PRESIDENT, AUDITOR) 찾기
		RoleType newRole = currentUserRoles.stream()
			.filter(role -> role.getRole() == RoleType.PRESIDENT
				|| role.getRole() == RoleType.VICE_PRESIDENT
				|| role.getRole() == RoleType.MANAGER)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("유효한 역할이 없습니다."))
			.getRole();

		// 새로운 역할 위임
		Role newDelegateRole = Role.builder()
			.role(newRole)
			.studentId(delegateUser.getId())
			.build();
		roleRepository.save(newDelegateRole);

		// STUDENT 역할을 제외한 모든 역할 삭제
		currentUserRoles.stream()
			.filter(role -> role.getRole() != RoleType.STUDENT) // STUDENT는 유지
			.forEach(roleRepository::delete);
	}
}
