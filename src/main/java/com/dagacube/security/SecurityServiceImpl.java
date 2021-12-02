package com.dagacube.security;

import com.dagacube.domain.repository.SystemUserRepository;
import com.dagacube.domain.repository.entity.DagacubeSystemUser;
import com.dagacube.exception.DagacubeUserAuthorizationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;

public class SecurityServiceImpl implements SecurityService {

	private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10, new SecureRandom());

	@Autowired
	private SystemUserRepository systemUserRepository;

	@Override
	public void verifyPassword(String username, String password) throws DagacubeUserAuthorizationException {

		DagacubeSystemUser user = systemUserRepository.findByUsername(username);

		if (user == null || !bCryptPasswordEncoder.matches(password, user.getPassword())) {
			throw new DagacubeUserAuthorizationException();
		}

	}
}
