package com.vini.barbershop.controller;

import com.vini.barbershop.dto.request.AgendamentoRequestDTO;
import com.vini.barbershop.dto.response.AgendamentoResponseDTO;
import com.vini.barbershop.service.AgendamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService service;

    // qualquer usuário logado pode agendar
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<AgendamentoResponseDTO> criar(@RequestBody AgendamentoRequestDTO dto) {
        return ResponseEntity.ok(service.criarAgendamento(dto));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<List<AgendamentoResponseDTO>> listarMeus() {
        return ResponseEntity.ok(service.listarMeusAgendamentos());
    }

    // somente ADMIN pode ver todos agendamentos
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<AgendamentoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarMeusAgendamentos());
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        service.cancelarAgendamento(id);
        return ResponseEntity.noContent().build();
    }
}