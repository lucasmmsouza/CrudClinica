package com.example.crudclinica.repository;

import com.example.crudclinica.model.Agenda;
import com.example.crudclinica.model.Medico;
import com.example.crudclinica.model.StatusAgenda;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    List<Agenda> findByMedicoAndStatusAndDataHoraBetween(Medico medico, StatusAgenda status, LocalDateTime inicio, LocalDateTime fim);
    List<Agenda> findByMedicoAndDataHoraBetween(Medico medico, LocalDateTime inicio, LocalDateTime fim);
    List<Agenda> findByStatus(StatusAgenda status);
}