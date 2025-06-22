package com.example.financas.service;

import com.example.financas.dto.DespesaDTO;
import com.example.financas.model.Despesa;
import com.example.financas.model.Usuario;
import com.example.financas.repository.DespesaRepository;
import com.example.financas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DespesaService {

    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario getUsuarioLogado(UserDetails userDetails) {
        return usuarioRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public DespesaDTO criar(DespesaDTO dto, UserDetails userDetails) {
        Usuario usuario = getUsuarioLogado(userDetails);
        Despesa despesa = Despesa.builder()
                .descricao(dto.getDescricao())
                .valor(dto.getValor())
                .data(dto.getData())
                .usuario(usuario)
                .build();
        despesa = despesaRepository.save(despesa);
        dto.setId(despesa.getId());
        return dto;
    }

    public List<DespesaDTO> listar(UserDetails userDetails) {
        Usuario usuario = getUsuarioLogado(userDetails);
        return despesaRepository.findAllByUsuario(usuario).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public DespesaDTO detalhar(Long id, UserDetails userDetails) {
        Usuario usuario = getUsuarioLogado(userDetails);
        Despesa despesa = despesaRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new RuntimeException("Despesa não encontrada"));
        return toDTO(despesa);
    }

    public DespesaDTO atualizar(Long id, DespesaDTO dto, UserDetails userDetails) {
        Usuario usuario = getUsuarioLogado(userDetails);
        Despesa despesa = despesaRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new RuntimeException("Despesa não encontrada"));

        despesa.setDescricao(dto.getDescricao());
        despesa.setValor(dto.getValor());
        despesa.setData(dto.getData());

        despesaRepository.save(despesa);
        return toDTO(despesa);
    }

    public void deletar(Long id, UserDetails userDetails) {
        Usuario usuario = getUsuarioLogado(userDetails);
        Despesa despesa = despesaRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new RuntimeException("Despesa não encontrada"));

        despesaRepository.delete(despesa);
    }

    private DespesaDTO toDTO(Despesa despesa) {
        return DespesaDTO.builder()
                .id(despesa.getId())
                .descricao(despesa.getDescricao())
                .valor(despesa.getValor())
                .data(despesa.getData())
                .build();
    }
}
