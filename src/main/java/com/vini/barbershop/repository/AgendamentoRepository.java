package com.vini.barbershop.repository;

import com.vini.barbershop.entity.Agendamento;
import com.vini.barbershop.entity.enums.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    boolean existsByDataAndHorario(LocalDate data, LocalTime horario);

    List<Agendamento> findByUsuarioId(Long usuarioId);

    List<Agendamento> findByData(LocalDate data);

    List<Agendamento> findByStatus(StatusAgendamento status);

    List<Agendamento> findByUsuarioIdAndStatus(Long usuarioId, StatusAgendamento status);
}