package com.example.financas.service;

import com.example.financas.dto.ReceitaDTO;
import com.example.financas.model.Receita;
import com.example.financas.model.Usuario;
import com.example.financas.repository.ReceitaRepository;
import com.example.financas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReceitaService {

    @Autowired
    private ReceitaRepository receitaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario getUsuarioLogado(UserDetails userDetails) {
        return usuarioRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public ReceitaDTO criar(ReceitaDTO dto, UserDetails userDetails) {
        Usuario usuario = getUsuarioLogado(userDetails);
        Receita receita = Receita.builder()
                .descricao(dto.getDescricao())
                .valor(dto.getValor())
                .data(dto.getData())
                .usuario(usuario)
                .build();
        receita = receitaRepository.save(receita);
        dto.setId(receita.getId());
        return dto;
    }

    public List<ReceitaDTO> listar(UserDetails userDetails) {
        Usuario usuario = getUsuarioLogado(userDetails);
        return receitaRepository.findAllByUsuario(usuario).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ReceitaDTO detalhar(Long id, UserDetails userDetails) {
        Usuario usuario = getUsuarioLogado(userDetails);
        Receita receita = receitaRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new RuntimeException("Receita não encontrada"));
        return toDTO(receita);
    }

    public ReceitaDTO atualizar(Long id, ReceitaDTO dto, UserDetails userDetails) {
        Usuario usuario = getUsuarioLogado(userDetails);
        Receita receita = receitaRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new RuntimeException("Receita não encontrada"));
        receita.setDescricao(dto.getDescricao());
        receita.setValor(dto.getValor());
        receita.setData(dto.getData());
        receitaRepository.save(receita);
        return toDTO(receita);
    }

    public void deletar(Long id, UserDetails userDetails) {
        Usuario usuario = getUsuarioLogado(userDetails);
        Receita receita = receitaRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new RuntimeException("Receita não encontrada"));
        receitaRepository.delete(receita);
    }

    private ReceitaDTO toDTO(Receita receita) {
        return ReceitaDTO.builder()
                .id(receita.getId())
                .descricao(receita.getDescricao())
                .valor(receita.getValor())
                .data(receita.getData())
                .build();
    }
}
