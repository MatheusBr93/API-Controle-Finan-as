package com.example.financas.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "investimentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Investimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String tipo;

    private BigDecimal valorAplicado;

    private LocalDate dataAplicacao;

    private Double percentualRetorno;

    private LocalDate dataResgate;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}
