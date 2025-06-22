package com.example.financas.controller;

import com.example.financas.dto.AdminEstatisticasDTO;
import com.example.financas.dto.GraficoDTO;
import com.example.financas.model.Usuario;
import com.example.financas.repository.UsuarioRepository;
import com.example.financas.service.AdminService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@SecurityRequirement(name = "bearer-key")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AdminService adminService;

    @GetMapping("/usuarios")
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @GetMapping("/estatisticas")
    public AdminEstatisticasDTO estatisticasGerais() {
        return adminService.estatisticasGerais();
    }

    @GetMapping("/estatisticas/mensais")
    public List<GraficoDTO> estatisticasMensais() {
        return adminService.estatisticasMensais();
    }
}
