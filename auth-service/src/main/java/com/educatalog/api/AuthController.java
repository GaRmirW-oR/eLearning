package com.educatalog.api;

import com.educatalog.api.dto.AuthResponse;
import com.educatalog.api.dto.LoginRequest;
import com.educatalog.app.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        String token = authService.login(request.username(), request.password());
        return new AuthResponse(token);
    }
}
