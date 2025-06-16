package com.example.crudclinica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Entity
public class Medico extends Pessoa {
    @NotBlank(message = "O CRM não pode estar em branco.")
    private String crm;

    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL)
    private List<Consulta> consultas;

    public Medico() {}

    public Medico(String nome, String crm) {
        setNome(nome);
        this.crm = crm;
    }

    public String getCrm() { return crm; }
    public void setCrm(String crm) { this.crm = crm; }

    public List<Consulta> getConsultas() { return consultas; }
    public void setConsultas(List<Consulta> consultas) { this.consultas = consultas; }

    public String dados() {
        return "Médico: " + getNome() + ", CRM: " + crm;
    }
}