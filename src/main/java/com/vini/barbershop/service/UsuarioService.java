package com.vini.barbershop.service;

import com.vini.barbershop.dto.request.UsuarioRequestDTO;
import com.vini.barbershop.dto.response.UsuarioResponseDTO;
import com.vini.barbershop.entity.enums.Role;
import com.vini.barbershop.entity.Usuario;
import com.vini.barbershop.mapper.UsuarioMapper;
import com.vini.barbershop.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final UsuarioMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public UsuarioResponseDTO criarUsuario(UsuarioRequestDTO dto) {

        Usuario usuario = mapper.toEntity(dto);

        // 🔐 Criptografar senha
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));

        // 🔥 Definir role padrão
        usuario.setRole(Role.CLIENTE);

        Usuario salvo = repository.save(usuario);

        return mapper.toResponseDTO(salvo);
    }

    public List<UsuarioResponseDTO> listarUsuarios() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return mapper.toResponseDTO(usuario);
    }

    public void deletarUsuario(Long id) {
        repository.deleteById(id);
    }
}