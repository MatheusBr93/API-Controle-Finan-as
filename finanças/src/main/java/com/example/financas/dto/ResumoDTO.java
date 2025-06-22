package com.example.financas.dto;

import java.math.BigDecimal;

public record ResumoDTO(
        BigDecimal totalReceitas,
        BigDecimal totalDespesas,
        BigDecimal totalInvestimentos
) {}
