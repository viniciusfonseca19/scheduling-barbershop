package com.vini.barbershop.mapper;

import com.vini.barbershop.dto.request.UsuarioRequestDTO;
import com.vini.barbershop.dto.response.UsuarioResponseDTO;
import com.vini.barbershop.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioRequestDTO dto) {

        Usuario usuario = new Usuario();

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        //senha é criptografada no service

        return usuario;
    }

    public UsuarioResponseDTO toResponseDTO(Usuario usuario) {

        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .role(usuario.getRole())
                .build();
    }
}