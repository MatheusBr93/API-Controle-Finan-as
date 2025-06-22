package com.example.financas.service;

import com.example.financas.dto.GraficoDTO;
import com.example.financas.dto.ResumoDTO;
import com.example.financas.model.Despesa;
import com.example.financas.model.Investimento;
import com.example.financas.model.Receita;
import com.example.financas.model.Usuario;
import com.example.financas.repository.DespesaRepository;
import com.example.financas.repository.InvestimentoRepository;
import com.example.financas.repository.ReceitaRepository;
import com.example.financas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private ReceitaRepository receitaRepository;

    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private InvestimentoRepository investimentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario getUsuario(UserDetails userDetails) {
        return usuarioRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public BigDecimal calcularSaldo(UserDetails userDetails) {
        Usuario usuario = getUsuario(userDetails);

        BigDecimal receitas = receitaRepository.findByUsuario(usuario).stream()
                .map(Receita::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal despesas = despesaRepository.findByUsuario(usuario).stream()
                .map(Despesa::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::subtract);

        return receitas.add(despesas);
    }

    public ResumoDTO resumo(UserDetails userDetails) {
        Usuario usuario = getUsuario(userDetails);

        BigDecimal totalReceitas = receitaRepository.findByUsuario(usuario).stream()
                .map(Receita::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDespesas = despesaRepository.findByUsuario(usuario).stream()
                .map(Despesa::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalInvestimentos = investimentoRepository.findByUsuario(usuario).stream()
                .map(Investimento::getValorAplicado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ResumoDTO(totalReceitas, totalDespesas, totalInvestimentos);
    }

    public List<GraficoDTO> dadosGrafico(UserDetails userDetails) {
        Usuario usuario = getUsuario(userDetails);

        Map<YearMonth, BigDecimal> receitasPorMes = receitaRepository.findByUsuario(usuario).stream()
                .collect(Collectors.groupingBy(
                        r -> YearMonth.from(r.getData()),
                        Collectors.reducing(BigDecimal.ZERO, Receita::getValor, BigDecimal::add)
                ));

        Map<YearMonth, BigDecimal> despesasPorMes = despesaRepository.findByUsuario(usuario).stream()
                .collect(Collectors.groupingBy(
                        d -> YearMonth.from(d.getData()),
                        Collectors.reducing(BigDecimal.ZERO, Despesa::getValor, BigDecimal::add)
                ));

        Set<YearMonth> todosMeses = new HashSet<>();
        todosMeses.addAll(receitasPorMes.keySet());
        todosMeses.addAll(despesasPorMes.keySet());

        return todosMeses.stream()
                .sorted()
                .map(mes -> new GraficoDTO(
                        mes,
                        receitasPorMes.getOrDefault(mes, BigDecimal.ZERO),
                        despesasPorMes.getOrDefault(mes, BigDecimal.ZERO)
                ))
                .collect(Collectors.toList());
    }
}
