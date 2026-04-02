package com.vini.barbershop.service;

import com.vini.barbershop.dto.request.AgendamentoRequestDTO;
import com.vini.barbershop.dto.response.AgendamentoResponseDTO;
import com.vini.barbershop.entity.Agendamento;
import com.vini.barbershop.entity.Usuario;
import com.vini.barbershop.mapper.AgendamentoMapper;
import com.vini.barbershop.repository.AgendamentoRepository;
import com.vini.barbershop.repository.UsuarioRepository;
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
    private final UsuarioRepository usuarioRepository;

    // log do usuario(simulação)
    private final UsuarioLogadoService usuarioLogadoService;

    public AgendamentoResponseDTO criarAgendamento(AgendamentoRequestDTO dto) {

        Agendamento agendamento = mapper.toEntity(dto);

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        agendamento.setUsuario(usuario);

        if (agendamento.getData().isBefore(LocalDate.now())) {
            throw new RuntimeException("Não é possível agendar para uma data no passado");
        }

        if (agendamento.getData().isEqual(LocalDate.now()) &&
                agendamento.getHorario().isBefore(LocalTime.now())) {
            throw new RuntimeException("Não é possível agendar para um horário que já passou");
        }

        boolean existe = repository.existsByDataAndHorario(
                agendamento.getData(),
                agendamento.getHorario()
        );

        if (existe) {
            throw new RuntimeException("Horário já está ocupado");
        }

        Agendamento salvo = repository.save(agendamento);

        return mapper.toResponseDTO(salvo);
    }

    public List<AgendamentoResponseDTO> listarAgendamentos() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    // listagem de agendamentos
    public List<AgendamentoResponseDTO> listarMeusAgendamentos() {

        Long usuarioId = usuarioLogadoService.getId();

        List<Agendamento> agendamentos = repository.findByUsuarioId(usuarioId);

        return agendamentos.stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    public void cancelarAgendamento(Long id) {
        repository.deleteById(id);
    }
}