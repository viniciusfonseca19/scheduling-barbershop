package com.vini.barbershop.service;

import com.vini.barbershop.dto.request.UsuarioRequestDTO;
import com.vini.barbershop.dto.response.UsuarioResponseDTO;
import com.vini.barbershop.entity.Usuario;
import com.vini.barbershop.entity.enums.Role;
import com.vini.barbershop.exception.BusinessException;
import com.vini.barbershop.exception.ResourceNotFoundException;
import com.vini.barbershop.mapper.UsuarioMapper;
import com.vini.barbershop.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final UsuarioMapper mapper;
    private final PasswordEncoder passwordEncoder;

    // cria usuário
    public UsuarioResponseDTO criarUsuario(UsuarioRequestDTO dto) {

        // validar email duplicado
        if (repository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Email já cadastrado");
        }

        Usuario usuario = mapper.toEntity(dto);

        // criptografar senha
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));

        // 👤 role padrão
        usuario.setRole(Role.CLIENTE);

        // usuário começa desbloqueado
        usuario.setBloqueado(false);

        Usuario salvo = repository.save(usuario);

        return mapper.toResponseDTO(salvo);
    }

    // lista usuários + paginação
    public Page<UsuarioResponseDTO> listarUsuarios(int page, int size) {

        PageRequest pageable = PageRequest.of(page, size);

        return repository.findAll(pageable)
                .map(mapper::toResponseDTO);
    }

    // busca por id
    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        return mapper.toResponseDTO(usuario);
    }

    // deleta usuário
    public void deletarUsuario(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Usuário não encontrado");
        }
        repository.deleteById(id);
    }

    // bloqueia usuário
    public void bloquearUsuario(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        usuario.setBloqueado(true);

        repository.save(usuario);
    }

    // desbloqueia usuário
    public void desbloquearUsuario(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        usuario.setBloqueado(false);

        repository.save(usuario);
    }
}