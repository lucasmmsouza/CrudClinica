package com.example.crudclinica.controller;

import com.example.crudclinica.model.Medico;
import com.example.crudclinica.repository.MedicoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/medicos")
public class MedicoWebController {
    private final MedicoRepository repository;

    public MedicoWebController(MedicoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("medicos", repository.findAll());
        return "medicolista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("medico", new Medico());
        return "medicoform";
    }

    @PostMapping("/salvar")
    public String salvar(Medico medico) {
        repository.save(medico);
        return "redirect:/web/medicos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("medico", repository.findById(id).orElse(new Medico()));
        return "medicoform";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/web/medicos";
    }
}