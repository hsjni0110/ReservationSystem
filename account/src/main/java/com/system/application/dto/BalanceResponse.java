package com.system.application.dto;

import java.math.BigDecimal;

public record BalanceResponse(
        BigDecimal totalAmount
) {
}
