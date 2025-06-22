package com.example.financas.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvestimentoDTO {
    private Long id;
    private String nome;
    private String tipo;
    private BigDecimal valorAplicado;
    private LocalDate dataAplicacao;
    private Double percentualRetorno;
    private LocalDate dataResgate;
}
