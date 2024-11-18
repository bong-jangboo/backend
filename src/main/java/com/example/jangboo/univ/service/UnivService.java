package com.example.jangboo.univ.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.jangboo.univ.controller.dto.request.RegisterRequest;
import com.example.jangboo.role.service.RoleService;
import com.example.jangboo.univ.controller.dto.response.UnivInfoResponse;
import com.example.jangboo.univ.domain.Univ;
import com.example.jangboo.univ.domain.UnivRepository;
import com.example.jangboo.users.service.UserService;

@Service
public class UnivService {
	private final UnivRepository univRepository;
	private final UserService userService;
	private final RoleService roleService;

	@Value("${jangboo.front_end.url}")
	private String pageUrl;

	public UnivService(UnivRepository univRepository, @Lazy UserService userService, RoleService roleService) {
		this.univRepository = univRepository;
		this.userService = userService;
		this.roleService = roleService;
	}

	@Transactional
	public Void registerUser(RegisterRequest request, Optional<Long> optionalId, String role) {
		Long parentId = optionalId.orElse(null);

		switch (role) {
			case "AUDITOR":
			case "PRESIDENT":
				registerAuditorOrPresident(request, parentId, role);
				break;

			case "STUDENT":
				registerUser(request, parentId, role);
				break;

			default:
				throw new IllegalArgumentException("Unknown role: " + role);
		}

		return null;
	}

	private void registerAuditorOrPresident(RegisterRequest request, Long parentId, String role) {
		Long orgId = registerOrg(request, parentId);
		registerUser(request, orgId, role);
	}

	private Long registerOrg(RegisterRequest request, Long parentId) {
		return Optional.ofNullable(parentId)
			.map(id -> createUnivByType(request.dept(), id))
			.orElseGet(() -> createUnivByType(request.college(), null));
	}

	private void registerUser(RegisterRequest request, Long parentId, String role) {
		Long userId = userService.registerUser(request, parentId);
		roleService.createRole(role, userId);
	}

	@Transactional
	public Long createUnivByType(String name, Long parentId) {
		Optional<Univ> parent = findParent(parentId);
		return univRepository.save(
			Univ.builder()
				.name(name)
				.orgType(determineOrgTypeByParentId(parent))
				.parent(parent.orElse(null))
				.build()
		).getId();
	}

	private String determineOrgTypeByParentId(Optional<Univ> parent) {
		return parent.isPresent() ? "DEPARTURE" : "COLLEGE";
	}

	private Optional<Univ> findParent(Long parentId) {
		return Optional.ofNullable(parentId).flatMap(univRepository::findById);
	}

	public String getSignUpLink(Long deptId) {
		String orgType = getOrgType(deptId);
		String singupLink = "";
		switch (orgType) {
			case "DEPARTURE":
				singupLink += String.format(pageUrl + "/pages/login&signup/signup.html?&deptId=%d&role=%s", deptId, "STUDENT");
				break;
			case "COLLEGE":
				singupLink += String.format(pageUrl + "/pages/login&signup/signup.html?&collegeId=%d&role=%s", deptId, "PRESIDENT");
				break;
		}
		return singupLink;
	}

	public String getOrgType(Long deptId) {
		return univRepository.findById(deptId).get().getOrgType().toString();
	}

	public UnivInfoResponse getParentInfo(Long childId) {
		Univ child = univRepository.findById(childId).orElseThrow(() -> new IllegalArgumentException("Univ not found"));
		String parentName = Optional.ofNullable(child.getParent())
			.map(Univ::getName)
			.orElse(null);

		return new UnivInfoResponse(parentName, child.getName());
	}
}
