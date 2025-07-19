package com.system.reservationsystem.controller;

import com.system.auth.domain.Auth;
import com.system.point.application.PointService;
import com.system.point.application.dto.PointHistoryResponse;
import com.system.point.application.dto.PointResponse;
import com.system.user.signup.application.UserService;
import com.system.user.signup.application.dto.UserProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final PointService pointService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getUserProfile(@Auth Long userId) {
        UserProfileResponse userProfile = userService.getUserProfile(userId);
        return ResponseEntity.ok(userProfile);
    }

    @GetMapping("/points")
    public ResponseEntity<PointResponse> getUserPoints(@Auth Long userId) {
        PointResponse pointResponse = pointService.getUserPoints(userId);
        return ResponseEntity.ok(pointResponse);
    }

    @GetMapping("/points/history")
    public ResponseEntity<List<PointHistoryResponse>> getUserPointHistory(@Auth Long userId) {
        List<PointHistoryResponse> history = pointService.getUserPointHistory(userId);
        return ResponseEntity.ok(history);
    }
}