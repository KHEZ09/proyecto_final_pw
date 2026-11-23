package com.example.demo.web;

import com.example.demo.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UsuarioController {
    private final UsuarioRepository usuarios;
    public UsuarioController(UsuarioRepository usuarios) { this.usuarios = usuarios; }
    @GetMapping("/me") public Object me(Authentication a) {
        var u = usuarios.findByUsername(a.getName()).orElseThrow();
        return java.util.Map.of("username", u.getUsername(), "nombre", u.getNombre(), "rol", u.getRol().name());
    }
}
