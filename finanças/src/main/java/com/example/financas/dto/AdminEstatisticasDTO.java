package com.example.financas.dto;

import java.math.BigDecimal;

public record AdminEstatisticasDTO(
        BigDecimal totalReceitas,
        BigDecimal totalDespesas,
        BigDecimal totalInvestimentos,
        BigDecimal saldoGeral
) {}
