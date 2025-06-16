package com.example.crudclinica.controller;

import com.example.crudclinica.model.Medico;
import com.example.crudclinica.repository.MedicoRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/medicos")
public class MedicoWebController {
    private final MedicoRepository repository;

    public MedicoWebController(MedicoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String listar(@RequestParam(name = "nome", required = false) String nome, Model model) {
        if (nome != null && !nome.isEmpty()) {
            model.addAttribute("medicos", repository.findByNomeContainingIgnoreCase(nome));
        } else {
            model.addAttribute("medicos", repository.findAll());
        }
        model.addAttribute("nomeFiltro", nome);
        return "medicolista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("medico", new Medico());
        return "medicoform";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("medico") Medico medico, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "medicoform";
        }
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