package com.vini.barbershop.dto.response;

import com.vini.barbershop.entity.enums.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UsuarioResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private Role role;
}