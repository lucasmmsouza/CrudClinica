package com.example.crudclinica.repository;

import com.example.crudclinica.model.Consulta;
import com.example.crudclinica.model.Medico;
import com.example.crudclinica.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
}