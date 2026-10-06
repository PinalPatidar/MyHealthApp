package com.health.app.service;

import com.health.app.domain.UserAccount;
import com.health.app.dto.auth.LoginRequest;
import com.health.app.dto.auth.LoginResponse;
import com.health.app.dto.auth.RegisterRequest;

public interface AuthenticationService {

    LoginResponse login(LoginRequest loginRequest);

    void register(RegisterRequest registerRequest);

}