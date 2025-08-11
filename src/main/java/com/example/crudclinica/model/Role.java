// src/main/java/com/example/crudclinica/model/Role.java
package com.example.crudclinica.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.List;

@Entity
public class Role implements GrantedAuthority, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome; // Ex: ROLE_ADMIN, ROLE_USER

    @ManyToMany(mappedBy = "roles")
    private List<Usuario> usuarios;

    @Override
    public String getAuthority() {
        return this.nome;
    }

    @Override
    public String toString() {
        return this.nome;
    }
    // -------------------------

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public List<Usuario> getUsuarios() { return usuarios; }
    public void setUsuarios(List<Usuario> usuarios) { this.usuarios = usuarios; }
}