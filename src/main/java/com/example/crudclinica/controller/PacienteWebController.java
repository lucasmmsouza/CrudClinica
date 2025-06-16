package com.example.crudclinica.controller;

import com.example.crudclinica.model.Paciente;
import com.example.crudclinica.repository.PacienteRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/pacientes")
public class PacienteWebController {
    private final PacienteRepository repository;

    public PacienteWebController(PacienteRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("pacientes", repository.findAll());
        return "pacientelista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("paciente", new Paciente());
        return "pacienteform";
    }

    @PostMapping("/salvar")
    public String salvar(Paciente paciente) {
        repository.save(paciente);
        return "redirect:/web/pacientes";
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