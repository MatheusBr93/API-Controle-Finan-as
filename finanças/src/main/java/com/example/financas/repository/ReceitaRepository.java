package com.example.financas.repository;

import com.example.financas.model.Receita;
import com.example.financas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface ReceitaRepository extends JpaRepository<Receita, Long> {

    List <Receita> findByUsuario(Usuario usuario);


    List<Receita> findAllByUsuario(Usuario usuario);

    Optional<Receita> findByIdAndUsuario(Long id, Usuario usuario);
}
