package com.example.crudclinica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

@Entity
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // O campo 'data' foi movido para a Agenda, mas pode ser mantido aqui por redundância se desejado.
    // Para este exemplo, vamos removê-lo para evitar duplicação de dados.

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
    @JoinColumn(name = "agenda_id", unique = true) // Garante que um slot de agenda só tenha uma consulta
    private Agenda agenda;


    public Consulta() {}

    public Consulta(Double valor, String observacao, Paciente paciente, Medico medico, Agenda agenda) {
        this.valor = valor;
        this.observacao = observacao;
        this.paciente = paciente;
        this.medico = medico;
        this.agenda = agenda;
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

    public String dados() {
        return "Consulta em: " + (agenda != null ? agenda.getDataHora() : "sem data") + ", Valor: " + valor + ", Obs: " + observacao;
    }

    // Adicionado para manter compatibilidade com o que existia no modelo anterior, buscando a data da agenda.
    @Transient
    public LocalDateTime getData() {
        return agenda != null ? agenda.getDataHora() : null;
    }
}