package com.dagacube.security;

import com.dagacube.exception.DagacubeUserAuthorizationException;

public interface SecurityService {
	void verifyPassword(String userName, String password) throws DagacubeUserAuthorizationException;
}
