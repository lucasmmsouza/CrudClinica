package com.example.crudclinica.controller;

import com.example.crudclinica.model.*;
import com.example.crudclinica.repository.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@RequestMapping("/web/consultas")
public class ConsultaWebController {
    private final ConsultaRepository consultaRepo;
    private final PacienteRepository pacienteRepo;
    private final MedicoRepository medicoRepo;
    private final AgendaRepository agendaRepo;
    private final Validator validator;
    private final TipoExameRepository tipoExameRepo;
    private final ExameRepository exameRepo;
    private final UsuarioRepository usuarioRepo;

    public ConsultaWebController(ConsultaRepository consultaRepo, PacienteRepository pacienteRepo, MedicoRepository medicoRepo, AgendaRepository agendaRepo, Validator validator, TipoExameRepository tipoExameRepo, ExameRepository exameRepo, UsuarioRepository usuarioRepo) {
        this.consultaRepo = consultaRepo;
        this.pacienteRepo = pacienteRepo;
        this.medicoRepo = medicoRepo;
        this.agendaRepo = agendaRepo;
        this.validator = validator;
        this.tipoExameRepo = tipoExameRepo;
        this.exameRepo = exameRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @GetMapping
    public String listar(@RequestParam(name = "data", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = usuarioRepo.findByUsuario(userDetails.getUsername());

        if (usuario.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            if (data != null) {
                model.addAttribute("consultas", consultaRepo.findByAgendaDataHoraBetween(data.atStartOfDay(), data.atTime(LocalTime.MAX)));
            } else {
                model.addAttribute("consultas", consultaRepo.findAll());
            }
        } else if (usuario.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MEDICO"))) {
            model.addAttribute("consultas", consultaRepo.findByMedicoUsuarioIdOrderByAgendaDataHoraDesc(usuario.getId()));
        } else { // Paciente
            model.addAttribute("consultas", consultaRepo.findByPacienteUsuarioIdOrderByAgendaDataHoraDesc(usuario.getId()));
        }
        model.addAttribute("dataFiltro", data);
        return "consultalista";
    }

    private void carregarDadosFormulario(Model model) {
        model.addAttribute("pacientes", pacienteRepo.findAll());
        model.addAttribute("medicos", medicoRepo.findAll());
        model.addAttribute("horariosDisponiveis", agendaRepo.findByStatus(StatusAgenda.DISPONIVEL));
        model.addAttribute("tiposExame", tipoExameRepo.findAll());
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("consulta", new Consulta());
        carregarDadosFormulario(model);
        return "consultaform";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute("consulta") Consulta consulta, BindingResult bindingResult,
                         @RequestParam(required = false) Long tipoExameId,
                         @RequestParam(required = false) String nomeExame,
                         @RequestParam(required = false) String observacoesExame,
                         Model model, @AuthenticationPrincipal UserDetails userDetails) {

        if (consulta.getAgenda() != null && consulta.getAgenda().getId() != null) {
            Agenda agenda = agendaRepo.findById(consulta.getAgenda().getId()).orElse(null);
            if (agenda != null) {
                consulta.setAgenda(agenda);
                consulta.setMedico(agenda.getMedico());
            }
        }

        Usuario usuario = usuarioRepo.findByUsuario(userDetails.getUsername());
        if (usuario.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            Paciente paciente = pacienteRepo.findByUsuarioId(usuario.getId());
            consulta.setPaciente(paciente);
        }

        validator.validate(consulta, bindingResult);

        if (bindingResult.hasErrors()) {
            carregarDadosFormulario(model);
            return "consultaform";
        }

        // Se a consulta for nova, atualiza a agenda
        if (consulta.getId() == null) {
            Agenda agenda = consulta.getAgenda();
            agenda.setStatus(StatusAgenda.AGENDADO);
            agenda.setConsulta(consulta);
            agendaRepo.save(agenda);
        }

        Consulta consultaSalva = consultaRepo.save(consulta);

        if (nomeExame != null && !nomeExame.trim().isEmpty() && tipoExameId != null) {
            TipoExame tipoExame = tipoExameRepo.findById(tipoExameId).orElse(null);
            if (tipoExame != null) {
                Exame novoExame = new Exame();
                novoExame.setTipoExame(tipoExame);
                novoExame.setNomeExame(nomeExame);
                novoExame.setObservacoes(observacoesExame);
                novoExame.setConsulta(consultaSalva);
                exameRepo.save(novoExame);
            }
        }

        return "redirect:/web/consultas";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("consulta", consultaRepo.findById(id).orElse(new Consulta()));
        carregarDadosFormulario(model);
        model.addAttribute("horariosDisponiveis", agendaRepo.findAll());
        return "consultaform";
    }

    @GetMapping("/marcar-realizada/{id}")
    public String marcarRealizada(@PathVariable Long id) {
        consultaRepo.findById(id).ifPresent(consulta -> {
            consulta.setStatus(StatusConsulta.REALIZADA);
            consultaRepo.save(consulta);
        });
        return "redirect:/web/consultas";
    }

    @GetMapping("/cancelar/{id}")
    public String cancelar(@PathVariable Long id) {
        consultaRepo.findById(id).ifPresent(consulta -> {
            consulta.setStatus(StatusConsulta.CANCELADA);
            consultaRepo.save(consulta);

            Agenda agenda = consulta.getAgenda();
            if (agenda != null) {
                agenda.setStatus(StatusAgenda.DISPONIVEL);
                agenda.setConsulta(null);
                agendaRepo.save(agenda);
            }
        });
        return "redirect:/web/consultas";
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