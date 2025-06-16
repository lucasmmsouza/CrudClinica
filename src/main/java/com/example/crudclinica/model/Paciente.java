package com.example.crudclinica.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Paciente extends Pessoa {
    private String telefone;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL)
    private List<Consulta> consultas;

    public Paciente() {}

    public Paciente(String nome, String telefone) {
        setNome(nome);
        this.telefone = telefone;
    }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public List<Consulta> getConsultas() { return consultas; }
    public void setConsultas(List<Consulta> consultas) { this.consultas = consultas; }

    public String dados() {
        return "Paciente: " + getNome() + ", Telefone: " + telefone;
    }
}