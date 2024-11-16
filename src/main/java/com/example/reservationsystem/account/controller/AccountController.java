package com.example.reservationsystem.account.controller;

import com.example.reservationsystem.account.application.AccountService;
import com.example.reservationsystem.account.dto.BalanceRequest;
import com.example.reservationsystem.account.dto.BalanceResponse;
import com.example.reservationsystem.auth.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/recharge")
    public BalanceResponse rechargeAccount(
            @Auth Long userId,
            @RequestBody BalanceRequest request
    ) {
        return accountService.recharge( userId, request.rechargeAmount() );
    }

}