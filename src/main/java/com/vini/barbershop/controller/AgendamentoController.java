package com.vini.barbershop.controller;

import com.vini.barbershop.dto.request.AgendamentoRequestDTO;
import com.vini.barbershop.dto.response.AgendamentoResponseDTO;
import com.vini.barbershop.service.AgendamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService service;

    @PostMapping
    public AgendamentoResponseDTO criar(@RequestBody @Valid AgendamentoRequestDTO dto) {
        return service.criarAgendamento(dto);
    }

    @GetMapping
    public List<AgendamentoResponseDTO> listar() {
        return service.listarAgendamentos();
    }

    @DeleteMapping("/{id}")
    public void cancelar(@PathVariable Long id) {
        service.cancelarAgendamento(id);
    }
}