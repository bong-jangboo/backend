package com.example.jangboo.users.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.jangboo.auth.controller.dto.Info.CurrentUserInfo;
import com.example.jangboo.global.dto.ResultDto;
import com.example.jangboo.users.controller.dto.request.UpdatePayedInfoRequest;
import com.example.jangboo.users.controller.dto.response.UserInfoResponse;
import com.example.jangboo.users.controller.dto.response.UserListResponse;
import com.example.jangboo.users.service.UserService;

import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/api/users")
public class UserController {
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("")
	public ResponseEntity<ResultDto<UserInfoResponse>> getUserInfo(
		@AuthenticationPrincipal CurrentUserInfo userInfo
	) {
		return ResponseEntity.ok(ResultDto.of(200,"사용자가 조회되었습니다",userService.getUserInfo(userInfo.userId(),
			userInfo.deptId())));
	}

	@PostMapping("/update-payed-info")
	public ResponseEntity<ResultDto<Void>> updatePayedInfo(
		@AuthenticationPrincipal CurrentUserInfo userInfo,
		@RequestBody Optional<UpdatePayedInfoRequest> request
	) {
		return ResponseEntity.ok(ResultDto.of(200,"사용자가 조회되었습니다",userService.registerPayedInfo(userInfo.userId(),request)));
	}

	@GetMapping ("/get-is-payed")
	public ResponseEntity<ResultDto<Boolean>> getIsPayed(
		@AuthenticationPrincipal CurrentUserInfo userInfo
	) {
		return ResponseEntity.ok(ResultDto.of(200,"사용자가 조회되었습니다",userService.getIsPayed(userInfo.userId())));
	}
	@GetMapping ("/payed-users/{pageNum}")
	public ResponseEntity<ResultDto<UserListResponse>> getPayedUsers(
		@AuthenticationPrincipal CurrentUserInfo userInfo,
		@PathVariable int pageNum,
		@RequestParam boolean isPayed
	) {
		return ResponseEntity.ok(ResultDto.of(200,"사용자가 조회되었습니다",userService.getPayedUsers(userInfo.deptId(),pageNum,isPayed)));
	}

}
