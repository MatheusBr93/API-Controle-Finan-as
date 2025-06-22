package com.example.financas.service;

import com.example.financas.dto.AdminEstatisticasDTO;
import com.example.financas.dto.GraficoDTO;
import com.example.financas.model.Despesa;
import com.example.financas.model.Investimento;
import com.example.financas.model.Receita;
import com.example.financas.repository.DespesaRepository;
import com.example.financas.repository.InvestimentoRepository;
import com.example.financas.repository.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private ReceitaRepository receitaRepository;

    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private InvestimentoRepository investimentoRepository;

    public AdminEstatisticasDTO estatisticasGerais() {
        BigDecimal totalReceitas = receitaRepository.findAll().stream()
                .map(Receita::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDespesas = despesaRepository.findAll().stream()
                .map(Despesa::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalInvestimentos = investimentoRepository.findAll().stream()
                .map(Investimento::getValorAplicado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal saldo = totalReceitas.subtract(totalDespesas);

        return new AdminEstatisticasDTO(totalReceitas, totalDespesas, totalInvestimentos, saldo);
    }

    public List<GraficoDTO> estatisticasMensais() {
        Map<YearMonth, BigDecimal> receitas = receitaRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        r -> YearMonth.from(r.getData()),
                        Collectors.reducing(BigDecimal.ZERO, Receita::getValor, BigDecimal::add)
                ));

        Map<YearMonth, BigDecimal> despesas = despesaRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        d -> YearMonth.from(d.getData()),
                        Collectors.reducing(BigDecimal.ZERO, Despesa::getValor, BigDecimal::add)
                ));

        Set<YearMonth> meses = new HashSet<>();
        meses.addAll(receitas.keySet());
        meses.addAll(despesas.keySet());

        return meses.stream()
                .sorted()
                .map(m -> new GraficoDTO(
                        m,
                        receitas.getOrDefault(m, BigDecimal.ZERO),
                        despesas.getOrDefault(m, BigDecimal.ZERO)
                ))
                .collect(Collectors.toList());
    }
}
