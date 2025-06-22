package com.example.financas.controller;

import com.example.financas.dto.ReceitaDTO;
import com.example.financas.service.ReceitaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/receitas")
class ReceitaController {

    @Autowired
    private ReceitaService receitaService;

    @PostMapping
    public ResponseEntity<ReceitaDTO> criar(@Valid @RequestBody ReceitaDTO dto, @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(receitaService.criar(dto, user));
    }

    @GetMapping
    public ResponseEntity<List<ReceitaDTO>> listar(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(receitaService.listar(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceitaDTO> detalhar(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(receitaService.detalhar(id, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReceitaDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ReceitaDTO dto, @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(receitaService.atualizar(id, dto, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        receitaService.deletar(id, user);
        return ResponseEntity.noContent().build();
    }
}
