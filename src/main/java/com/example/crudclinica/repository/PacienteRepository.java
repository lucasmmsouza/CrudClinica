package com.example.crudclinica.repository;

import com.example.crudclinica.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    List<Paciente> findByNomeContainingIgnoreCase(String nome);
    Paciente findByUsuarioId(Long id);
}