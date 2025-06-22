package com.example.financas.controller;

import com.example.financas.dto.InvestimentoDTO;
import com.example.financas.model.Usuario;
import com.example.financas.repository.UsuarioRepository;
import com.example.financas.service.InvestimentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/investimentos")
public class InvestimentoController {

    @Autowired
    private InvestimentoService investimentoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailUsuario = authentication.getName();
        return usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado no banco de dados."));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<InvestimentoDTO> criar(@RequestBody InvestimentoDTO dto) {
        Usuario usuario = getUsuarioAutenticado();
        InvestimentoDTO novoInvestimento = investimentoService.criar(dto, usuario);
        return new ResponseEntity<>(novoInvestimento, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<InvestimentoDTO>> listar() {
        Usuario usuario = getUsuarioAutenticado();
        List<InvestimentoDTO> investimentos = investimentoService.listar(usuario);
        return ResponseEntity.ok(investimentos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<InvestimentoDTO> detalhar(@PathVariable Long id) {
        Usuario usuario = getUsuarioAutenticado();
        InvestimentoDTO investimento = investimentoService.detalhar(id, usuario);
        return ResponseEntity.ok(investimento);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<InvestimentoDTO> atualizar(@PathVariable Long id, @RequestBody InvestimentoDTO dto) {
        Usuario usuario = getUsuarioAutenticado();
        InvestimentoDTO updatedInvestimento = investimentoService.atualizar(id, dto, usuario);
        return ResponseEntity.ok(updatedInvestimento);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        Usuario usuario = getUsuarioAutenticado();
        investimentoService.deletar(id, usuario);
        return ResponseEntity.noContent().build();
    }
}