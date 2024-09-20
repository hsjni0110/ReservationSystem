package com.example.reservationsystem.user.signup.presentation;

import com.example.reservationsystem.user.signup.application.SignUpService;
import com.example.reservationsystem.user.signup.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sign-up")
public class SignUpController {

    private final SignUpService signUpService;

    @PostMapping
    public ResponseEntity<Void> signUp(
            @RequestBody SignUpRequest signUpRequest
    ) {
        signUpService.signUp(signUpRequest.email(), signUpRequest.password(), signUpRequest.name(), signUpRequest.phoneNumber());
        return ResponseEntity.ok().build();
    }

}
