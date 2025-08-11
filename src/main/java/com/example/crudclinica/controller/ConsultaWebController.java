package com.example.crudclinica.controller;

import com.example.crudclinica.model.Agenda;
import com.example.crudclinica.model.Consulta;
import com.example.crudclinica.model.StatusAgenda;
import com.example.crudclinica.repository.AgendaRepository;
import com.example.crudclinica.repository.ConsultaRepository;
import com.example.crudclinica.repository.MedicoRepository;
import com.example.crudclinica.repository.PacienteRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.Validator; // << IMPORT CORRIGIDO

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@RequestMapping("/web/consultas")
public class ConsultaWebController {
    private final ConsultaRepository consultaRepo;
    private final PacienteRepository pacienteRepo;
    private final MedicoRepository medicoRepo;
    private final AgendaRepository agendaRepo;
    private final Validator validator; // << TIPO CORRIGIDO

    // Construtor corrigido para usar org.springframework.validation.Validator
    public ConsultaWebController(ConsultaRepository consultaRepo, PacienteRepository pacienteRepo, MedicoRepository medicoRepo, AgendaRepository agendaRepo, Validator validator) {
        this.consultaRepo = consultaRepo;
        this.pacienteRepo = pacienteRepo;
        this.medicoRepo = medicoRepo;
        this.agendaRepo = agendaRepo;
        this.validator = validator;
    }

    @GetMapping
    public String listar(@RequestParam(name = "data", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data, Model model) {
        if (data != null) {
            model.addAttribute("consultas", consultaRepo.findByAgendaDataHoraBetween(data.atStartOfDay(), data.atTime(LocalTime.MAX)));
        } else {
            model.addAttribute("consultas", consultaRepo.findAll());
        }
        model.addAttribute("dataFiltro", data);
        return "consultalista";
    }

    private void carregarDadosFormulario(Model model) {
        model.addAttribute("pacientes", pacienteRepo.findAll());
        model.addAttribute("medicos", medicoRepo.findAll());
        model.addAttribute("horariosDisponiveis", agendaRepo.findByStatus(StatusAgenda.DISPONIVEL));
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("consulta", new Consulta());
        carregarDadosFormulario(model);
        return "consultaform";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute("consulta") Consulta consulta, BindingResult bindingResult, Model model) {

        if (consulta.getAgenda() != null && consulta.getAgenda().getId() != null) {
            Agenda agenda = agendaRepo.findById(consulta.getAgenda().getId()).orElse(null);
            if (agenda != null) {
                consulta.setAgenda(agenda);
                consulta.setMedico(agenda.getMedico());
            }
        }

        // Esta chamada agora funcionará corretamente
        validator.validate(consulta, bindingResult);

        if (bindingResult.hasErrors()) {
            carregarDadosFormulario(model);
            return "consultaform";
        }

        Agenda agenda = consulta.getAgenda();
        agenda.setStatus(StatusAgenda.AGENDADO);
        agenda.setConsulta(consulta);

        consultaRepo.save(consulta);
        agendaRepo.save(agenda);

        return "redirect:/web/consultas";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("consulta", consultaRepo.findById(id).orElse(new Consulta()));
        carregarDadosFormulario(model);
        model.addAttribute("horariosDisponiveis", agendaRepo.findAll());
        return "consultaform";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        consultaRepo.findById(id).ifPresent(consulta -> {
            Agenda agenda = consulta.getAgenda();
            if (agenda != null) {
                agenda.setStatus(StatusAgenda.DISPONIVEL);
                agenda.setConsulta(null);
                agendaRepo.save(agenda);
            }
            consultaRepo.deleteById(id);
        });

        return "redirect:/web/consultas";
    }
}