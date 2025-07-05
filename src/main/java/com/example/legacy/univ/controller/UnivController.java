package com.example.legacy.univ.controller;

import java.util.Optional;

import com.example.legacy.univ.service.UnivService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.legacy.auth.controller.dto.Info.CurrentUserInfo;
import com.example.legacy.univ.controller.dto.request.RegisterRequest;
import com.example.legacy.global.dto.ResultDto;
import com.example.legacy.univ.controller.dto.response.UnivInfoResponse;

@RestController
@RequestMapping("/api/univ")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class UnivController {
	private final UnivService univService;

	public UnivController(UnivService univService) {
		this.univService = univService;
	}

	/*parentId 는 유저별 상위 소속 개념 ( 단과대는 부모 X, 학과의 부모는 단과대, 학생의 부모는 학과 )*/
	@PostMapping("/register/{role}")
	public ResponseEntity<ResultDto<Void>> signup(
		@RequestBody RegisterRequest request,
		@RequestParam("parentId") Optional<Long> parentId,
		@PathVariable("role") String role) {
		return ResponseEntity.ok(ResultDto.of(200,"가입이 완료되었습니다", univService.registerUser(request,parentId,role)));
	}

	@GetMapping("/signup-link")
	public ResponseEntity<ResultDto<String>> getUnivSignupLink(
		@AuthenticationPrincipal CurrentUserInfo userInfo
	){
		return ResponseEntity.ok(ResultDto.of(
			200,"가입링크가 성공적으로 반환되었습니다.", univService.getSignUpLink(userInfo.deptId())));
	}

	@GetMapping("/")
	public ResponseEntity<ResultDto<UnivInfoResponse>> getUnivInfo(
		@AuthenticationPrincipal CurrentUserInfo userInfo,
		@RequestParam Long deptId
	){
		return ResponseEntity.ok(ResultDto.of(
			200,"학과정보가 성공적으로 반환되었습니다.", univService.getParentInfo(deptId)));
	}
}
