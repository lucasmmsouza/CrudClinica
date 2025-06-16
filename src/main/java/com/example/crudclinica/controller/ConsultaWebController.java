package com.example.crudclinica.controller;

import com.example.crudclinica.model.Consulta;
import com.example.crudclinica.repository.ConsultaRepository;
import com.example.crudclinica.repository.MedicoRepository;
import com.example.crudclinica.repository.PacienteRepository;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

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
    public String listar(@RequestParam(name = "data", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data, Model model) {
        if (data != null) {
            model.addAttribute("consultas", consultaRepo.findByDataBetween(data.atStartOfDay(), data.atTime(LocalTime.MAX)));
        } else {
            model.addAttribute("consultas", consultaRepo.findAll());
        }
        model.addAttribute("dataFiltro", data);
        return "consultalista";
    }

    private void carregarDadosFormulario(Model model) {
        model.addAttribute("pacientes", pacienteRepo.findAll());
        model.addAttribute("medicos", medicoRepo.findAll());
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("consulta", new Consulta());
        carregarDadosFormulario(model);
        return "consultaform";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("consulta") Consulta consulta, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            carregarDadosFormulario(model); // Recarrega os dropdowns se houver erro
            return "consultaform";
        }
        consultaRepo.save(consulta);
        return "redirect:/web/consultas";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("consulta", consultaRepo.findById(id).orElse(new Consulta()));
        carregarDadosFormulario(model);
        return "consultaform";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        consultaRepo.deleteById(id);
        return "redirect:/web/consultas";
    }
}