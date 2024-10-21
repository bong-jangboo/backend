package com.example.jangboo.role.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jangboo.auth.controller.dto.Info.CurrentUserInfo;
import com.example.jangboo.global.dto.ResultDto;
import com.example.jangboo.role.service.RoleService;

@RestController
@RequestMapping("/api/role")
public class RoleController {
	private final RoleService roleService;

	public RoleController(RoleService roleService) {
		this.roleService = roleService;
	}

	@GetMapping("/main")
	public ResponseEntity<ResultDto<String>> getMainPage(
		@AuthenticationPrincipal CurrentUserInfo userInfo
	){
		return ResponseEntity.ok(ResultDto.of(200,"메인 페이지 url이 반환되었습니다",roleService.getMainPageUrl(userInfo.userId())));
	}
}
