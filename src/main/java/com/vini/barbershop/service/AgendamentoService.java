package com.vini.barbershop.service;

import com.vini.barbershop.dto.request.AgendamentoRequestDTO;
import com.vini.barbershop.dto.response.AgendamentoResponseDTO;
import com.vini.barbershop.entity.Agendamento;
import com.vini.barbershop.repository.AgendamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository repository;

    public AgendamentoResponseDTO criarAgendamento(AgendamentoRequestDTO dto) {

        Agendamento agendamento = new Agendamento();
        agendamento.setNomeCliente(dto.getNomeCliente());
        agendamento.setData(dto.getData());
        agendamento.setHorario(dto.getHorario());
        agendamento.setTipoCorte(dto.getTipoCorte());

        //  Regra: não permitir data no passado
        if (agendamento.getData().isBefore(LocalDate.now())) {
            throw new RuntimeException("Não é possível agendar para uma data no passado");
        }

        //  Regra: não permitir horário passado no mesmo dia
        if (agendamento.getData().isEqual(LocalDate.now()) &&
                agendamento.getHorario().isBefore(LocalTime.now())) {
            throw new RuntimeException("Não é possível agendar para um horário que já passou");
        }

        //  Regra: não permitir horário duplicado
        boolean existe = repository.existsByDataAndHorario(
                agendamento.getData(),
                agendamento.getHorario()
        );

        if (existe) {
            throw new RuntimeException("Horário já está ocupado");
        }

        Agendamento salvo = repository.save(agendamento);

        return toResponseDTO(salvo);
    }

    public List<AgendamentoResponseDTO> listarAgendamentos() {
        return repository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public void cancelarAgendamento(Long id) {
        repository.deleteById(id);
    }

    private AgendamentoResponseDTO toResponseDTO(Agendamento agendamento) {
        AgendamentoResponseDTO dto = new AgendamentoResponseDTO();
        dto.setId(agendamento.getId());
        dto.setNomeCliente(agendamento.getNomeCliente());
        dto.setData(agendamento.getData());
        dto.setHorario(agendamento.getHorario());
        dto.setTipoCorte(agendamento.getTipoCorte());
        return dto;
    }
}