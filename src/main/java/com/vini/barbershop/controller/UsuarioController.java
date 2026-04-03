package com.vini.barbershop.controller;

import com.vini.barbershop.dto.request.UsuarioRequestDTO;
import com.vini.barbershop.dto.response.UsuarioResponseDTO;
import com.vini.barbershop.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    // lista usuários + paginação
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UsuarioResponseDTO>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(service.listarUsuarios(page, size));
    }

    // busca por id
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // deleta usuário
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    // bloqueia usuário
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/bloquear")
    public ResponseEntity<Void> bloquear(@PathVariable Long id) {
        service.bloquearUsuario(id);
        return ResponseEntity.noContent().build();
    }

    // desbloqueia usuário
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/desbloquear")
    public ResponseEntity<Void> desbloquear(@PathVariable Long id) {
        service.desbloquearUsuario(id);
        return ResponseEntity.noContent().build();
    }
}