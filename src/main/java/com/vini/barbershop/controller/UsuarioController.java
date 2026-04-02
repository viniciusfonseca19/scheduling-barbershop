package com.vini.barbershop.controller;

import com.vini.barbershop.entity.Usuario;
import com.vini.barbershop.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;

    @PostMapping
    public Usuario criar(@RequestBody Usuario usuario) {
        return service.criarUsuario(usuario);
    }
}