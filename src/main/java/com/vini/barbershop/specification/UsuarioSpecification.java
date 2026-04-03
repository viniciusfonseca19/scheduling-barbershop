package com.vini.barbershop.specification;

import com.vini.barbershop.entity.Usuario;
import org.springframework.data.jpa.domain.Specification;

public class UsuarioSpecification {

    public static Specification<Usuario> nomeContains(String nome) {
        return (root, query, criteriaBuilder) ->
                nome == null ? null :
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("nome")),
                                "%" + nome.toLowerCase() + "%"
                        );
    }

    public static Specification<Usuario> emailContains(String email) {
        return (root, query, criteriaBuilder) ->
                email == null ? null :
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("email")),
                                "%" + email.toLowerCase() + "%"
                        );
    }
}