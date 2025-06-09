package com.example.jangboo.users.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="users")
public class User {
	@Getter
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Getter
	@Column(name="dept_id")
	private Long deptId;

	@Getter
	@Column(name="name")
	private String name;

	@Getter
	@Column(name="login_id")
	private String loginId;

	@Getter
	@Column(name="password")
	private String password;

	@Getter
	@Column(name="number")
	private String number;

	@Getter
	@Column(nullable = true)
	private Boolean isPayed;

	// 소셜 로그인
	@Column(name = "auth_provider")
	@Enumerated(EnumType.STRING)
	private AuthProvider authProvider;

	@Column(name ="social_id", unique = true)
	private String socialId;

	@Builder
	public User(Long deptId, String name, String loginId, String password, String number) {
		this.name = name;
		this.deptId = deptId;
		this.loginId = loginId;
		this.password = password;
		this.number = number;
		this.isPayed=false;
	}

	@Builder
	public User(Long deptId, String name, String loginId, String password, String number, AuthProvider authProvider, String socialId) {
		this.deptId = deptId;
		this.name = name;
		this.loginId = loginId;
		this.password = password;
		this.number = number;
		this.authProvider = authProvider;
		this.socialId = socialId;
		this.isPayed = false;
	}


	public void updatePayedInfo(){
		this.isPayed = true;
	}
}
