package com.vini.barbershop.service;

import com.vini.barbershop.entity.Usuario;
import com.vini.barbershop.entity.enums.Role;
import com.vini.barbershop.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;

    public Usuario criarUsuario(Usuario usuario) {

        // Regra: se senha for essa, o usuário é admin(BARBEIRO)
        if ("bGPm5nWA".equals(usuario.getSenha())) {
            usuario.setRole(Role.ADMIN);
        } else {
            usuario.setRole(Role.CLIENT);
        }

        return repository.save(usuario);
    }
}