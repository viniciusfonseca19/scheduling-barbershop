package com.vini.barbershop.repository;

import com.vini.barbershop.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    boolean existsByDataAndHorario(LocalDate data, LocalTime horario);

    List<Agendamento> findByUsuarioId(Long usuarioId);
}