package com.example.crudclinica.controller;

import com.example.crudclinica.dto.CadastroDTO;
import com.example.crudclinica.model.*;
import com.example.crudclinica.repository.MedicoRepository;
import com.example.crudclinica.repository.PacienteRepository;
import com.example.crudclinica.repository.RoleRepository;
import com.example.crudclinica.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class CadastroController {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final PasswordEncoder passwordEncoder;

    public CadastroController(UsuarioRepository usuarioRepository, RoleRepository roleRepository, PacienteRepository pacienteRepository, MedicoRepository medicoRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/cadastro")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("cadastroDTO", new CadastroDTO());
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String processarCadastro(@Valid @ModelAttribute("cadastroDTO") CadastroDTO cadastroDTO, BindingResult bindingResult) {
        if (usuarioRepository.findByUsuario(cadastroDTO.getUsuario()) != null) {
            bindingResult.addError(new FieldError("cadastroDTO", "usuario", "Nome de usuário já existe."));
        }

        if ("PACIENTE".equals(cadastroDTO.getTipoUsuario()) && (cadastroDTO.getTelefone() == null || cadastroDTO.getTelefone().isBlank())) {
            bindingResult.addError(new FieldError("cadastroDTO", "telefone", "O telefone é obrigatório para pacientes."));
        }

        if ("MEDICO".equals(cadastroDTO.getTipoUsuario()) && (cadastroDTO.getCrm() == null || cadastroDTO.getCrm().isBlank())) {
            bindingResult.addError(new FieldError("cadastroDTO", "crm", "O CRM é obrigatório para médicos."));
        }

        if (bindingResult.hasErrors()) {
            return "cadastro";
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setUsuario(cadastroDTO.getUsuario());
        novoUsuario.setSenha(passwordEncoder.encode(cadastroDTO.getSenha()));

        if ("PACIENTE".equals(cadastroDTO.getTipoUsuario())) {
            Role userRole = roleRepository.findAll().stream().filter(r -> r.getNome().equals("ROLE_USER")).findFirst().orElseThrow(() -> new RuntimeException("Erro: Role 'ROLE_USER' não encontrada."));
            novoUsuario.setRoles(Collections.singletonList(userRole));

            Paciente paciente = new Paciente();
            paciente.setNome(cadastroDTO.getNome());
            paciente.setTelefone(cadastroDTO.getTelefone());
            paciente.setUsuario(novoUsuario);
            pacienteRepository.save(paciente);
        } else { // MEDICO
            List<Role> roles = new ArrayList<>();
            roles.add(roleRepository.findAll().stream().filter(r -> r.getNome().equals("ROLE_USER")).findFirst().orElseThrow(() -> new RuntimeException("Erro: Role 'ROLE_USER' não encontrada.")));
            roles.add(roleRepository.findAll().stream().filter(r -> r.getNome().equals("ROLE_MEDICO")).findFirst().orElseThrow(() -> new RuntimeException("Erro: Role 'ROLE_MEDICO' não encontrada.")));
            novoUsuario.setRoles(roles);

            Medico medico = new Medico();
            medico.setNome(cadastroDTO.getNome());
            medico.setCrm(cadastroDTO.getCrm());
            medico.setUsuario(novoUsuario);
            medicoRepository.save(medico);
        }

        return "redirect:/login?cadastro=sucesso";
    }
}