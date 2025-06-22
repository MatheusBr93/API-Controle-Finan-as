package com.example.financas.repository;

import com.example.financas.model.Despesa;
import com.example.financas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface DespesaRepository extends JpaRepository<Despesa, Long> {

    List<Despesa> findAllByUsuario(Usuario usuario);

    Optional<Despesa> findByIdAndUsuario(Long id, Usuario usuario);

    List <Despesa> findByUsuario(Usuario usuario);
}
