package com.example.reservationsystem.account.presentation.controller;

import com.example.reservationsystem.account.application.AccountService;
import com.example.reservationsystem.account.application.dto.BalanceRequest;
import com.example.reservationsystem.account.application.dto.BalanceResponse;
import com.example.reservationsystem.auth.domain.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<BalanceResponse> getBalance(
            @Auth Long userId
    ) {
        BalanceResponse balance = accountService.getBalance( userId );
        return ResponseEntity.ok( balance );
    }

    @PostMapping("/recharge")
    public ResponseEntity<BalanceResponse> rechargeAccount(
            @Auth Long userId,
            @RequestBody BalanceRequest request
    ) {
        BalanceResponse recharge = accountService.recharge( userId, request.rechargeAmount() );
        return ResponseEntity.ok( recharge );
    }

}
