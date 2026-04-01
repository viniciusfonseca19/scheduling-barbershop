package com.vini.barbershop.controller;

import com.vini.barbershop.entity.Agendamento;
import com.vini.barbershop.service.AgendamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService service;

    @PostMapping
    public Agendamento criar(@RequestBody Agendamento agendamento) {
        return service.criarAgendamento(agendamento);
    }

    @DeleteMapping("/{id}")
    public void cancelar(@PathVariable Long id) {
        service.cancelarAgendamento(id);
    }
    @GetMapping
    public List<Agendamento> listar() {
        return service.listarAgendamentos();
    }
}