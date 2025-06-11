package com.system.reservationsystem.controller;

import com.system.application.AccountService;
import com.system.application.dto.BalanceRequest;
import com.system.application.dto.BalanceResponse;
import com.system.auth.domain.Auth;
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
    public ResponseEntity<Void> rechargeAccount(
            @Auth Long userId,
            @RequestBody BalanceRequest request
    ) {
        accountService.recharge(userId, request.rechargeAmount());
        return ResponseEntity.ok().build();
    }

}
