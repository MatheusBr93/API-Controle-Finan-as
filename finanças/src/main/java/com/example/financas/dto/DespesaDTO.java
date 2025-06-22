package com.example.financas.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DespesaDTO {

    private Long id;

    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
    private BigDecimal valor;

    @NotNull(message = "Data é obrigatória")
    private LocalDate data;
}
