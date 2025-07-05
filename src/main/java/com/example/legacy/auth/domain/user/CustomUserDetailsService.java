package com.example.legacy.auth.domain.user;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.legacy.role.domain.Role;
import com.example.legacy.role.domain.RoleRepository;
import com.example.legacy.users.domain.User;
import com.example.legacy.users.domain.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

	public CustomUserDetailsService(UserRepository userRepository, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
		Optional<User> user = Optional.ofNullable(userRepository.findByLoginId(loginId)
			.orElseThrow(() -> new NoSuchElementException("user:" + loginId + " not found.")));

		List<Role> roles = roleRepository.findByStudentId(user.get().getId());

		User currentUser = user.get();

		return new CustomUserDetails(currentUser.getId(),currentUser.getDeptId(),currentUser.getLoginId(), currentUser.getPassword(),roles);
	}

	public UserDetails loadUserById(Long userId) throws UsernameNotFoundException {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new NoSuchElementException("user with id: " + userId + " not found."));

		List<Role> roles = roleRepository.findByStudentId(user.getId());

		return new CustomUserDetails(user.getId(), user.getDeptId(), user.getLoginId(), user.getPassword(), roles);
	}
}
