package com.vini.barbershop.repository;

import com.vini.barbershop.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    Optional<Agendamento> findByDataAndHorario(LocalDate data, LocalTime horario);
}