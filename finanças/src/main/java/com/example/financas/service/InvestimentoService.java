package com.example.financas.service;

import com.example.financas.dto.InvestimentoDTO;
import com.example.financas.model.Investimento;
import com.example.financas.model.Usuario;
import com.example.financas.repository.InvestimentoRepository;
import com.example.financas.repository.UsuarioRepository;

import org.springframework.beans.BeanUtils; // Para copiar propriedades (cuidado com nomes diferentes)
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvestimentoService {

    @Autowired
    private InvestimentoRepository investimentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;


    private InvestimentoDTO convertToDTO(Investimento investimento) {
        return InvestimentoDTO.builder()
                .id(investimento.getId())
                .nome(investimento.getNome())
                .tipo(investimento.getTipo())
                .valorAplicado(investimento.getValorAplicado())
                .dataAplicacao(investimento.getDataAplicacao())
                .percentualRetorno(investimento.getPercentualRetorno())
                .dataResgate(investimento.getDataResgate())
                .build();
    }


    public InvestimentoDTO criar(InvestimentoDTO dto, Usuario usuario) {
        Investimento investimento = new Investimento();
        investimento.setNome(dto.getNome());
        investimento.setTipo(dto.getTipo());
        investimento.setValorAplicado(dto.getValorAplicado());
        investimento.setDataAplicacao(dto.getDataAplicacao());
        investimento.setPercentualRetorno(dto.getPercentualRetorno());
        investimento.setDataResgate(dto.getDataResgate());
        investimento.setUsuario(usuario); // ASSOCIA O INVESTIMENTO AO USUÁRIO AQUI

        Investimento savedInvestimento = investimentoRepository.save(investimento);
        return convertToDTO(savedInvestimento);
    }


    public List<InvestimentoDTO> listar(Usuario usuario) {
        return investimentoRepository.findByUsuario(usuario).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public InvestimentoDTO detalhar(Long id, Usuario usuario) {
        Investimento investimento = investimentoRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new RuntimeException("Investimento não encontrado ou não pertence ao usuário."));
        return convertToDTO(investimento);
    }


    public InvestimentoDTO atualizar(Long id, InvestimentoDTO dto, Usuario usuario) {
        Investimento investimento = investimentoRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new RuntimeException("Investimento não encontrado ou não pertence ao usuário."));


        investimento.setNome(dto.getNome());
        investimento.setTipo(dto.getTipo());
        investimento.setValorAplicado(dto.getValorAplicado());
        investimento.setDataAplicacao(dto.getDataAplicacao());
        investimento.setPercentualRetorno(dto.getPercentualRetorno());
        investimento.setDataResgate(dto.getDataResgate());


        Investimento updatedInvestimento = investimentoRepository.save(investimento);
        return convertToDTO(updatedInvestimento);
    }


    public void deletar(Long id, Usuario usuario) {
        if (!investimentoRepository.existsByIdAndUsuario(id, usuario)) {
            throw new RuntimeException("Investimento não encontrado ou não pertence ao usuário.");
        }
        investimentoRepository.deleteById(id);
    }
}