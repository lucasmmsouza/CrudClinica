package com.example.crudclinica.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime data;
    private double valor;
    private String observacao;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Medico medico;

    public Consulta() {}

    public Consulta(LocalDateTime data, double valor, String observacao, Paciente paciente, Medico medico) {
        this.data = data;
        this.valor = valor;
        this.observacao = observacao;
        this.paciente = paciente;
        this.medico = medico;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime data) { this.data = data; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }

    public String dados() {
        return "Consulta em: " + data + ", Valor: " + valor + ", Obs: " + observacao;
    }
}