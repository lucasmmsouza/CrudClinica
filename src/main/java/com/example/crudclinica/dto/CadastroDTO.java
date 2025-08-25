package com.example.crudclinica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CadastroDTO {

    @NotBlank(message = "O nome não pode estar em branco.")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
    private String nome;

    @NotBlank(message = "O usuário não pode estar em branco.")
    @Size(min = 3, message = "O usuário deve ter no mínimo 3 caracteres.")
    private String usuario;

    @NotBlank(message = "A senha não pode estar em branco.")
    @Size(min = 4, message = "A senha deve ter no mínimo 4 caracteres.")
    private String senha;

    @NotBlank(message = "Você deve selecionar um tipo de usuário.")
    private String tipoUsuario; // "PACIENTE" ou "MEDICO"

    // Campos do Paciente
    private String telefone;

    // Campos do Médico
    private String crm;

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getCrm() { return crm; }
    public void setCrm(String crm) { this.crm = crm; }
}