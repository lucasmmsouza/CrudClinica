package com.example.crudclinica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Entity
public class Paciente extends Pessoa {
    @NotBlank(message = "O telefone não pode estar em branco.")
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