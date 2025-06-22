package com.example.financas.repository;

import com.example.financas.model.Investimento;
import com.example.financas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvestimentoRepository extends JpaRepository<Investimento, Long> {

    List<Investimento> findByUsuario(Usuario usuario);


    Optional<Investimento> findByIdAndUsuario(Long id, Usuario usuario);


    boolean existsByIdAndUsuario(Long id, Usuario usuario);
}