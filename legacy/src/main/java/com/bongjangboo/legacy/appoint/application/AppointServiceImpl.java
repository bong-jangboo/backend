package com.bongjangboo.legacy.appoint.application;

import com.bongjangboo.legacy.appoint.dto.AppointRequestDto;
import com.bongjangboo.legacy.role.domain.Role;
import com.bongjangboo.legacy.role.domain.RoleRepository;
import com.bongjangboo.legacy.role.domain.RoleType;
import com.bongjangboo.legacy.users.domain.User;
import com.bongjangboo.legacy.users.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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

        // 현재 userId와 newRoleType이 동일한 역할을 가진 경우 예외 발생
        roleRepository.findByStudentIdAndRole(user.getId(), newRoleType).ifPresent(existingRole -> {
            throw new IllegalArgumentException("해당 사용자는 이미 " + newRoleType + " 역할을 가지고 있습니다.");
        });

        // 모든 기존 역할의 endDate를 현재 날짜로 설정
        userRoles.forEach(role -> {
            role.setEndDate(LocalDate.now()); // endDate를 현재 날짜로 설정
            roleRepository.save(role); // 업데이트된 role 저장
        });

        // 새로운 역할 저장
        Role newRole = Role.builder()
                .role(newRoleType)
                .studentId(user.getId())
                .build();

        roleRepository.save(newRole);
    }
}
