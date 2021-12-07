package com.dagacube.domain.service;

import com.dagacube.exception.SystemUserAuthorizationException;

public interface SecurityService {
	void verifyPassword(String userName, String password) throws SystemUserAuthorizationException;
}
