package com.example.crudclinica.controller;

import com.example.crudclinica.model.Endereco; // IMPORT ADICIONADO
import com.example.crudclinica.model.Paciente;
import com.example.crudclinica.model.Role;
import com.example.crudclinica.model.Usuario;
import com.example.crudclinica.repository.PacienteRepository;
import com.example.crudclinica.repository.RoleRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Optional; // IMPORT ADICIONADO

@Controller
@RequestMapping("/web/pacientes")
public class PacienteWebController {
    private final PacienteRepository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;


    public PacienteWebController(PacienteRepository repository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, RestTemplate restTemplate) {
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.restTemplate = restTemplate;
    }

    private void carregarDadosDoFormulario(Model model) {
        String url = "https://servicodados.ibge.gov.br/api/v1/localidades/estados?orderBy=nome";
        Object[] estados = restTemplate.getForObject(url, Object[].class);
        model.addAttribute("estados", estados);
    }


    @GetMapping("/novo")
    public String novo(Model model) {
        Paciente paciente = new Paciente();
        paciente.setEndereco(new Endereco()); // GARANTE QUE O ENDEREÇO NUNCA SEJA NULO
        model.addAttribute("paciente", paciente);
        carregarDadosDoFormulario(model);
        return "pacienteform";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("paciente") Paciente paciente, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            carregarDadosDoFormulario(model);
            return "pacienteform";
        }

        if (paciente.getId() == null) {
            Usuario novoUsuario = new Usuario();
            String username = paciente.getNome().toLowerCase().split(" ")[0] + "." + paciente.getNome().toLowerCase().split(" ")[1];
            novoUsuario.setUsuario(username);
            novoUsuario.setSenha(passwordEncoder.encode(paciente.getTelefone()));

            Role userRole = (Role) roleRepository.findAll().stream().filter(r -> r.getNome().equals("ROLE_USER")).findFirst().orElse(null);
            novoUsuario.setRoles(Collections.singletonList(userRole));
            paciente.setUsuario(novoUsuario);
        }

        repository.save(paciente);
        return "redirect:/web/pacientes"; // Corrigido para /web/pacientes em vez de /login
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
                paciente.setEndereco(new Endereco()); // GARANTE QUE O ENDEREÇO NUNCA SEJA NULO
            }
            model.addAttribute("paciente", paciente);
        } else {
            // Se não encontrar, redireciona para a lista (ou mostra uma página de erro)
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
}