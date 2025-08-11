// src/main/java/com/example/crudclinica/config/UsuarioDetailsConfig.java
package com.example.crudclinica.config;

import com.example.crudclinica.model.Usuario;
import com.example.crudclinica.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public class UsuarioDetailsConfig implements UserDetailsService {

    private final UsuarioRepository repository;

    public UsuarioDetailsConfig(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("=================================================");
        System.out.println("Tentando carregar o usuário: " + username);

        Usuario usuario = repository.findByUsuario(username);

        if (usuario == null) {
            System.out.println("ERRO: Usuário '" + username + "' não encontrado no banco de dados.");
            System.out.println("=================================================");
            throw new UsernameNotFoundException("Usuário não encontrado!");
        }

        System.out.println("Usuário encontrado: " + usuario.getUsername());
        System.out.println("Senha (hash) do banco: " + usuario.getPassword());
        System.out.println("Papéis (Roles) do usuário: " + usuario.getAuthorities());
        System.out.println("=================================================");

        return new User(usuario.getUsername(), usuario.getPassword(), true, true, true, true, usuario.getAuthorities());
    }
}