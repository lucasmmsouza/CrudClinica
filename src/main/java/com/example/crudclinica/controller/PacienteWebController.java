package com.example.crudclinica.controller;

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
import java.util.Collections;

@Controller
@RequestMapping("/web/pacientes")
public class PacienteWebController {
    private final PacienteRepository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    public PacienteWebController(PacienteRepository repository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("paciente", new Paciente());
        return "pacienteform";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("paciente") Paciente paciente, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "pacienteform";
        }

        // Se for um novo paciente, cria um usuário para ele
        if (paciente.getId() == null) {
            Usuario novoUsuario = new Usuario();
            // Lógica simples para criar um nome de usuário. Ex: 'joao.silva'
            String username = paciente.getNome().toLowerCase().split(" ")[0] + "." + paciente.getNome().toLowerCase().split(" ")[1];
            novoUsuario.setUsuario(username);
            // Senha padrão é o telefone (deve ser trocada no primeiro login)
            novoUsuario.setSenha(passwordEncoder.encode(paciente.getTelefone()));

            Role userRole = (Role) roleRepository.findAll().stream().filter(r -> r.getNome().equals("ROLE_USER")).findFirst().orElse(null);
            novoUsuario.setRoles(Collections.singletonList(userRole));
            paciente.setUsuario(novoUsuario);
        }

        repository.save(paciente);
        return "redirect:/login"; // Redireciona para o login após o cadastro
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
        model.addAttribute("paciente", repository.findById(id).orElse(new Paciente()));
        return "pacienteform";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/web/pacientes";
    }
}