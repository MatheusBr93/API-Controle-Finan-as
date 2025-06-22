package com.example.financas.dto;

import java.math.BigDecimal;
import java.time.YearMonth;

public record GraficoDTO(
        YearMonth mes,
        BigDecimal totalReceitas,
        BigDecimal totalDespesas
) {}
