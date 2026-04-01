package com.vini.barbershop.service;

import com.vini.barbershop.entity.Agendamento;
import com.vini.barbershop.repository.AgendamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository repository;

    public Agendamento criarAgendamento(Agendamento agendamento) {

        //  não permiti horário duplicado
        repository.findByDataAndHorario(agendamento.getData(), agendamento.getHorario())
                .ifPresent(a -> {
                    throw new RuntimeException("Horário já está ocupado");
                });

        return repository.save(agendamento);
    }

    public void cancelarAgendamento(Long id) {
        repository.deleteById(id);
    }
}