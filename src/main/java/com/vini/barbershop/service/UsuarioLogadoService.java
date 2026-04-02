package com.vini.barbershop.service;

import com.vini.barbershop.entity.Usuario;
import com.vini.barbershop.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioLogadoService {

    private final UsuarioRepository repository;

    public Usuario getUsuarioLogado() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
}