package com.vini.barbershop.service;

import com.vini.barbershop.dto.request.AgendamentoRequestDTO;
import com.vini.barbershop.dto.response.AgendamentoResponseDTO;
import com.vini.barbershop.entity.Agendamento;
import com.vini.barbershop.entity.enums.Role;
import com.vini.barbershop.entity.enums.StatusAgendamento;
import com.vini.barbershop.entity.Usuario;
import com.vini.barbershop.exception.BusinessException;
import com.vini.barbershop.exception.ResourceNotFoundException;
import com.vini.barbershop.mapper.AgendamentoMapper;
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
    private final AgendamentoMapper mapper;
    private final UsuarioLogadoService usuarioLogadoService;

    public AgendamentoResponseDTO criarAgendamento(AgendamentoRequestDTO dto) {

        Usuario usuarioLogado = usuarioLogadoService.getUsuarioLogado();

        Agendamento agendamento = mapper.toEntity(dto);

        agendamento.setUsuario(usuarioLogado);
        agendamento.setStatus(StatusAgendamento.AGENDADO);

        if (agendamento.getData().isBefore(LocalDate.now())) {
            throw new BusinessException("Não é possível agendar para uma data no passado");
        }

        if (agendamento.getData().isEqual(LocalDate.now()) &&
                agendamento.getHorario().isBefore(LocalTime.now())) {
            throw new BusinessException("Não é possível agendar para um horário que já passou");
        }

        boolean existe = repository.existsByDataAndHorario(
                agendamento.getData(),
                agendamento.getHorario()
        );

        if (existe) {
            throw new BusinessException("Horário já está ocupado");
        }

        Agendamento salvo = repository.save(agendamento);

        return mapper.toResponseDTO(salvo);
    }

    public List<AgendamentoResponseDTO> listarMeusAgendamentos() {

        Usuario usuario = usuarioLogadoService.getUsuarioLogado();

        List<Agendamento> agendamentos;

        if (usuario.getRole() == Role.ADMIN) {
            agendamentos = repository.findAll();
        } else {
            agendamentos = repository.findByUsuarioId(usuario.getId());
        }

        return agendamentos.stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    public void cancelarAgendamento(Long id) {

        Agendamento agendamento = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado"));

        Usuario usuario = usuarioLogadoService.getUsuarioLogado();

        if (usuario.getRole() != Role.ADMIN &&
                !agendamento.getUsuario().getId().equals(usuario.getId())) {
            throw new BusinessException("Você não pode cancelar esse agendamento");
        }

        if (agendamento.getStatus() == StatusAgendamento.CANCELADO) {
            throw new BusinessException("Agendamento já está cancelado");
        }

        agendamento.setStatus(StatusAgendamento.CANCELADO);

        repository.save(agendamento);
    }

    public void finalizarAgendamento(Long id) {

        Agendamento agendamento = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado"));

        Usuario usuario = usuarioLogadoService.getUsuarioLogado();

        if (usuario.getRole() != Role.ADMIN) {
            throw new BusinessException("Apenas ADMIN pode finalizar agendamentos");
        }

        if (agendamento.getStatus() == StatusAgendamento.CANCELADO) {
            throw new BusinessException("Não é possível finalizar um agendamento cancelado");
        }

        if (agendamento.getStatus() == StatusAgendamento.FINALIZADO) {
            throw new BusinessException("Agendamento já foi finalizado");
        }

        agendamento.setStatus(StatusAgendamento.FINALIZADO);

        repository.save(agendamento);
    }

    // agendamento
    public List<String> listarHorariosDisponiveis(LocalDate data) {

        if (data.isBefore(LocalDate.now())) {
            throw new BusinessException("Não é possível consultar horários no passado");
        }

        List<LocalTime> horariosPadrao = List.of(
                LocalTime.of(8, 0),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                LocalTime.of(13, 0),
                LocalTime.of(14, 0),
                LocalTime.of(15, 0),
                LocalTime.of(16, 0),
                LocalTime.of(17, 0)
        );

        List<Agendamento> agendamentos = repository.findByData(data);

        List<LocalTime> ocupados = agendamentos.stream()
                .filter(a -> a.getStatus() != StatusAgendamento.CANCELADO)
                .map(Agendamento::getHorario)
                .toList();

        return horariosPadrao.stream()
                .filter(h -> !ocupados.contains(h))
                .map(LocalTime::toString)
                .toList();
    }
}