package com.example.crudclinica.repository;

import com.example.crudclinica.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    List<Medico> findByNomeContainingIgnoreCase(String nome);
    Medico findByUsuarioId(Long usuarioId);
}