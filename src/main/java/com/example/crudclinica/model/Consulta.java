package com.example.crudclinica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O valor é obrigatório.")
    @Positive(message = "O valor da consulta deve ser positivo.")
    private Double valor;

    private String observacao;

    @NotNull(message = "O paciente é obrigatório.")
    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @NotNull(message = "O médico é obrigatório.")
    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Medico medico;

    @OneToOne
    @JoinColumn(name = "agenda_id", unique = true)
    private Agenda agenda;

    @OneToMany(mappedBy = "consulta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Exame> exames = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private StatusConsulta status; // NOVO CAMPO


    public Consulta() {
        this.status = StatusConsulta.AGENDADA; // Define o status padrão
    }

    public Consulta(Double valor, String observacao, Paciente paciente, Medico medico, Agenda agenda) {
        this.valor = valor;
        this.observacao = observacao;
        this.paciente = paciente;
        this.medico = medico;
        this.agenda = agenda;
        this.status = StatusConsulta.AGENDADA; // Define o status padrão
    }

    // Getters e Setters...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }
    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }
    public Agenda getAgenda() { return agenda; }
    public void setAgenda(Agenda agenda) { this.agenda = agenda; }
    public List<Exame> getExames() { return exames; }
    public void setExames(List<Exame> exames) { this.exames = exames; }
    public StatusConsulta getStatus() { return status; } // GETTER
    public void setStatus(StatusConsulta status) { this.status = status; } // SETTER

    public String dados() {
        return "Consulta em: " + (agenda != null ? agenda.getDataHora() : "sem data") + ", Valor: " + valor + ", Obs: " + observacao;
    }

    @Transient
    public LocalDateTime getData() {
        return agenda != null ? agenda.getDataHora() : null;
    }
}