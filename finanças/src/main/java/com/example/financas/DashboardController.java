package com.example.financas;

import com.example.financas.service.DashboardService;
import com.example.financas.dto.ResumoDTO;
import com.example.financas.dto.GraficoDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
@SecurityRequirement(name = "bearer-key")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/saldo")
    public BigDecimal saldo(@AuthenticationPrincipal UserDetails userDetails) {
        return dashboardService.calcularSaldo(userDetails);
    }

    @GetMapping("/resumo")
    public ResumoDTO resumo(@AuthenticationPrincipal UserDetails userDetails) {
        return dashboardService.resumo(userDetails);
    }

    @GetMapping("/grafico")
    public List<GraficoDTO> grafico(@AuthenticationPrincipal UserDetails userDetails) {
        return dashboardService.dadosGrafico(userDetails);
    }
}
