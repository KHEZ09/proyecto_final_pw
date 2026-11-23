package com.example.demo.config;

import com.example.demo.model.*;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Bean CommandLineRunner seed(UsuarioRepository usuarios, PasswordEncoder encoder) {
        return ignoredArgs -> {
            guardarUsuario(usuarios, encoder, "admin", "admin123", "Administrador", Rol.ADMINISTRADOR);
            guardarUsuario(usuarios, encoder, "tecnico1", "tecnico123", "Técnico de mantenimiento", Rol.TECNICO);
            guardarUsuario(usuarios, encoder, "usuario1", "usuario123", "Usuario de prueba", Rol.USUARIO);
        };
    }

    private void guardarUsuario(UsuarioRepository usuarios, PasswordEncoder encoder, String username,
                                String password, String nombre, Rol rol) {
        var usuario = usuarios.findByUsername(username).orElseGet(Usuario::new);
        usuario.setUsername(username);
        usuario.setPassword(encoder.encode(password));
        usuario.setNombre(nombre);
        usuario.setRol(rol);
        usuarios.save(usuario);
    }
}
