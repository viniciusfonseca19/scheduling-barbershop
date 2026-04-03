package com.vini.barbershop.service;

import com.vini.barbershop.dto.request.UsuarioRequestDTO;
import com.vini.barbershop.dto.response.UsuarioResponseDTO;
import com.vini.barbershop.entity.Usuario;
import com.vini.barbershop.entity.enums.Role;
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

    // criar usuário
    public UsuarioResponseDTO criarUsuario(UsuarioRequestDTO dto) {

        Usuario usuario = mapper.toEntity(dto);

        // criptografar senha
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));

        // role padrão
        usuario.setRole(Role.CLIENTE);

        // usuário começa desbloqueado
        usuario.setBloqueado(false);

        Usuario salvo = repository.save(usuario);

        return mapper.toResponseDTO(salvo);
    }

    // listar usuário
    public List<UsuarioResponseDTO> listarUsuarios() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    // buscar usuário
    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return mapper.toResponseDTO(usuario);
    }

    // deletar usuário
    public void deletarUsuario(Long id) {
        repository.deleteById(id);
    }

    // bloquear usuárrio
    public void bloquearUsuario(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setBloqueado(true);

        repository.save(usuario);
    }

    // desbloquear usuário
    public void desbloquearUsuario(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setBloqueado(false);

        repository.save(usuario);
    }
}