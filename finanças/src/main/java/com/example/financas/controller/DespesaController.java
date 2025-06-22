package com.example.financas.controller;

import com.example.financas.dto.DespesaDTO;
import com.example.financas.service.DespesaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/despesas")
@Tag(name = "Despesas", description = "Endpoints para controle de despesas")
public class DespesaController {

    @Autowired
    private DespesaService despesaService;

    @PostMapping
    public DespesaDTO criar(@Valid @RequestBody DespesaDTO dto,
                            @AuthenticationPrincipal UserDetails userDetails) {
        return despesaService.criar(dto, userDetails);
    }

    @GetMapping
    public List<DespesaDTO> listar(@AuthenticationPrincipal UserDetails userDetails) {
        return despesaService.listar(userDetails);
    }

    @GetMapping("/{id}")
    public DespesaDTO detalhar(@PathVariable Long id,
                               @AuthenticationPrincipal UserDetails userDetails) {
        return despesaService.detalhar(id, userDetails);
    }

    @PutMapping("/{id}")
    public DespesaDTO atualizar(@PathVariable Long id,
                                @Valid @RequestBody DespesaDTO dto,
                                @AuthenticationPrincipal UserDetails userDetails) {
        return despesaService.atualizar(id, dto, userDetails);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id,
                        @AuthenticationPrincipal UserDetails userDetails) {
        despesaService.deletar(id, userDetails);
    }
}
