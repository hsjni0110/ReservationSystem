package com.system.reservationsystem.controller;

import com.system.user.signin.application.SignInService;
import com.system.user.signin.application.dto.SignInRequest;
import com.system.user.signin.application.dto.SignInResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sign-in")
public class SignInController {

    private final SignInService signInService;

    @PostMapping
    public ResponseEntity<SignInResponse> signIn(
            @RequestBody SignInRequest request
    ) {
        SignInResponse signInResponse = signInService.signIn(request.email(), request.password());
        return ResponseEntity.ok()
                .body(signInResponse);
    }

}
