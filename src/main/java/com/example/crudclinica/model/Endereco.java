package com.example.crudclinica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "A rua não pode estar em branco.")
    private String rua;
    @NotBlank(message = "O número não pode estar em branco.")
    private String numero;
    @NotBlank(message = "O bairro não pode estar em branco.")
    private String bairro;
    @NotBlank(message = "O CEP não pode estar em branco.")
    private String cep;
    @NotBlank(message = "A cidade não pode estar em branco.")
    private String cidade;
    @NotBlank(message = "O estado não pode estar em branco.")
    private String estado; // Armazenará a sigla do estado (UF)

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRua() { return rua; }
    public void setRua(String rua) { this.rua = rua; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        if (rua == null || cidade == null || estado == null) {
            return "";
        }
        return rua + ", " + numero + ", " + bairro + ", " + cidade + "/" + estado + " - " + cep;
    }
}