package com.vini.barbershop.controller;

import com.vini.barbershop.dto.request.UsuarioRequestDTO;
import com.vini.barbershop.dto.response.UsuarioResponseDTO;
import com.vini.barbershop.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;

    // cria usuário
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criar(@RequestBody @Valid UsuarioRequestDTO dto) {
        return ResponseEntity.ok(service.criarUsuario(dto));
    }

    // admin lista
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarUsuarios());
    }

    // admin busca por id
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // admin deleta usuário
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    // admin bloqueia usuário
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/bloquear")
    public ResponseEntity<Void> bloquear(@PathVariable Long id) {
        service.bloquearUsuario(id);
        return ResponseEntity.noContent().build();
    }

    // admin desbloqueia usuário
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/desbloquear")
    public ResponseEntity<Void> desbloquear(@PathVariable Long id) {
        service.desbloquearUsuario(id);
        return ResponseEntity.noContent().build();
    }
}