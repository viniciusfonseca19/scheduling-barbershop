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

        //Regra: não permitir data no passado
        if (agendamento.getData().isBefore(LocalDate.now())) {
            throw new BusinessException("Não é possível agendar para uma data no passado");
        }

        //Regra: horário passado no mesmo dia
        if (agendamento.getData().isEqual(LocalDate.now()) &&
                agendamento.getHorario().isBefore(LocalTime.now())) {
            throw new BusinessException("Não é possível agendar para um horário que já passou");
        }

        //Regra: não permitir horário duplicado
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

    //ADMIN vê todos | CLIENTE vê só os seus
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

        // CLIENTE só cancela o próprio
        if (usuario.getRole() != Role.ADMIN &&
                !agendamento.getUsuario().getId().equals(usuario.getId())) {
            throw new BusinessException("Você não pode cancelar esse agendamento");
        }

        agendamento.setStatus(StatusAgendamento.CANCELADO);

        repository.save(agendamento);
    }
}