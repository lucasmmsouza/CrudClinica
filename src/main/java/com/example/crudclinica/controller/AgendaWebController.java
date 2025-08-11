package com.example.crudclinica.controller;

import com.example.crudclinica.model.Agenda;
import com.example.crudclinica.model.StatusAgenda;
import com.example.crudclinica.repository.AgendaRepository;
import com.example.crudclinica.repository.MedicoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/web/agenda")
public class AgendaWebController {

    private final AgendaRepository agendaRepository;
    private final MedicoRepository medicoRepository;

    public AgendaWebController(AgendaRepository agendaRepository, MedicoRepository medicoRepository) {
        this.agendaRepository = agendaRepository;
        this.medicoRepository = medicoRepository;
    }

    @GetMapping
    public String listarHorarios(Model model) {
        model.addAttribute("agendas", agendaRepository.findAll());
        return "agendalista";
    }

    @GetMapping("/novo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("medicos", medicoRepository.findAll());
        return "agendaform";
    }

    @PostMapping("/gerar-horarios")
    public String gerarHorarios(@RequestParam Long medicoId,
                                @RequestParam LocalDate dataInicio,
                                @RequestParam LocalDate dataFim,
                                @RequestParam LocalTime horaInicio,
                                @RequestParam LocalTime horaFim,
                                @RequestParam int duracao) {

        medicoRepository.findById(medicoId).ifPresent(medico -> {
            List<LocalDate> datas = new ArrayList<>();
            LocalDate dataAtual = dataInicio;
            while (!dataAtual.isAfter(dataFim)) {
                if (dataAtual.getDayOfWeek() != DayOfWeek.SATURDAY && dataAtual.getDayOfWeek() != DayOfWeek.SUNDAY) {
                    datas.add(dataAtual);
                }
                dataAtual = dataAtual.plusDays(1);
            }

            for (LocalDate data : datas) {
                LocalTime horaAtual = horaInicio;
                while (horaAtual.isBefore(horaFim)) {
                    Agenda agenda = new Agenda();
                    agenda.setMedico(medico);
                    agenda.setDataHora(data.atTime(horaAtual));
                    agenda.setStatus(StatusAgenda.DISPONIVEL);
                    agendaRepository.save(agenda);
                    horaAtual = horaAtual.plusMinutes(duracao);
                }
            }
        });

        return "redirect:/web/agenda";
    }
}