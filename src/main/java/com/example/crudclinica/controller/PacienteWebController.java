package com.example.crudclinica.controller;

import com.example.crudclinica.model.Endereco;
import com.example.crudclinica.model.Paciente;
import com.example.crudclinica.model.Usuario;
import com.example.crudclinica.repository.ConsultaRepository;
import com.example.crudclinica.repository.PacienteRepository;
import com.example.crudclinica.repository.UsuarioRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Controller
@RequestMapping("/web/pacientes")
public class PacienteWebController {
    private final PacienteRepository repository;
    private final RestTemplate restTemplate;
    private final ConsultaRepository consultaRepository;
    private final UsuarioRepository usuarioRepository;

    public PacienteWebController(PacienteRepository repository, RestTemplate restTemplate, ConsultaRepository consultaRepository, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.restTemplate = restTemplate;
        this.consultaRepository = consultaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    private void carregarDadosDoFormulario(Model model) {
        String url = "https://servicodados.ibge.gov.br/api/v1/localidades/estados?orderBy=nome";
        Object[] estados = restTemplate.getForObject(url, Object[].class);
        model.addAttribute("estados", estados);
    }

    @GetMapping
    public String listar(@RequestParam(name = "nome", required = false) String nome, Model model) {
        if (nome != null && !nome.isEmpty()) {
            model.addAttribute("pacientes", repository.findByNomeContainingIgnoreCase(nome));
        } else {
            model.addAttribute("pacientes", repository.findAll());
        }
        model.addAttribute("nomeFiltro", nome);
        return "pacientelista";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Optional<Paciente> pacienteOpt = repository.findById(id);
        if (pacienteOpt.isPresent()) {
            Paciente paciente = pacienteOpt.get();
            if (paciente.getEndereco() == null) {
                paciente.setEndereco(new Endereco());
            }
            model.addAttribute("paciente", paciente);
        } else {
            return "redirect:/web/pacientes";
        }

        carregarDadosDoFormulario(model);
        return "pacienteform";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/web/pacientes";
    }

    @GetMapping("/historico/{id}")
    public String historico(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = usuarioRepository.findByUsuario(userDetails.getUsername());
        Paciente paciente = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paciente inválido:" + id));

        // Permite acesso se for ADMIN ou o próprio paciente dono do histórico
        if (usuario.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) || paciente.getUsuario().getId().equals(usuario.getId())) {
            model.addAttribute("paciente", paciente);
            model.addAttribute("consultas", consultaRepository.findByPacienteIdOrderByAgendaDataHoraDesc(id));
            return "historicoPaciente";
        } else {
            return "error/access-denied";
        }
    }
}