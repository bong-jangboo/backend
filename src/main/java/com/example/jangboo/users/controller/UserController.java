package com.example.jangboo.users.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jangboo.auth.controller.dto.Info.CurrentUserInfo;
import com.example.jangboo.global.dto.ResultDto;
import com.example.jangboo.users.controller.dto.response.UserInfoResponse;
import com.example.jangboo.users.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("")
	public ResponseEntity<ResultDto<UserInfoResponse>> getUser(
		@AuthenticationPrincipal CurrentUserInfo userInfo
	) {
		return ResponseEntity.ok(ResultDto.of(200,"사용자 정보가 조회되었습니다",userService.getUserInfo(userInfo.userId(),
			userInfo.deptId())));
	}

}
