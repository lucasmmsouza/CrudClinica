package com.example.crudclinica.controller;

import com.example.crudclinica.model.Consulta;
import com.example.crudclinica.model.Paciente;
import com.example.crudclinica.model.Medico;
import com.example.crudclinica.repository.ConsultaRepository;
import com.example.crudclinica.repository.PacienteRepository;
import com.example.crudclinica.repository.MedicoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/consultas")
public class ConsultaWebController {
    private final ConsultaRepository consultaRepo;
    private final PacienteRepository pacienteRepo;
    private final MedicoRepository medicoRepo;

    public ConsultaWebController(ConsultaRepository consultaRepo, PacienteRepository pacienteRepo, MedicoRepository medicoRepo) {
        this.consultaRepo = consultaRepo;
        this.pacienteRepo = pacienteRepo;
        this.medicoRepo = medicoRepo;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("consultas", consultaRepo.findAll());
        return "consultalista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("consulta", new Consulta());
        model.addAttribute("pacientes", pacienteRepo.findAll());
        model.addAttribute("medicos", medicoRepo.findAll());
        return "consultaform";
    }

    @PostMapping("/salvar")
    public String salvar(Consulta consulta) {
        consultaRepo.save(consulta);
        return "redirect:/web/consultas";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("consulta", consultaRepo.findById(id).orElse(new Consulta()));
        model.addAttribute("pacientes", pacienteRepo.findAll());
        model.addAttribute("medicos", medicoRepo.findAll());
        return "consultaform";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        consultaRepo.deleteById(id);
        return "redirect:/web/consultas";
    }
}