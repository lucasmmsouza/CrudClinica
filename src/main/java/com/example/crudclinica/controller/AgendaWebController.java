package com.example.crudclinica.controller;

import com.example.crudclinica.model.Agenda;
import com.example.crudclinica.model.StatusAgenda;
import com.example.crudclinica.model.Usuario;
import com.example.crudclinica.repository.AgendaRepository;
import com.example.crudclinica.repository.MedicoRepository;
import com.example.crudclinica.repository.UsuarioRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final UsuarioRepository usuarioRepository;

    public AgendaWebController(AgendaRepository agendaRepository, MedicoRepository medicoRepository, UsuarioRepository usuarioRepository) {
        this.agendaRepository = agendaRepository;
        this.medicoRepository = medicoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public String listarHorarios(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = usuarioRepository.findByUsuario(userDetails.getUsername());

        if (usuario.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            model.addAttribute("agendas", agendaRepository.findAll());
        } else if (usuario.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MEDICO"))) {
            model.addAttribute("agendas", agendaRepository.findByMedicoUsuarioId(usuario.getId()));
        } else { // Paciente
            model.addAttribute("agendas", agendaRepository.findByStatus(StatusAgenda.DISPONIVEL));
        }

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