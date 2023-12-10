package com.upao.recicla.domain.service;

import com.upao.recicla.domain.entity.Recompensa;
import com.upao.recicla.infra.repository.RecompensaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecompensaService {

    @Autowired
    public final RecompensaRepository recompensaRepository;

    public RecompensaService(RecompensaRepository recompensaRepository) {
        this.recompensaRepository = recompensaRepository;
    }
    public Recompensa addRecompensa(Recompensa recompensa) {
        if (recompensa.getValor()<=0){
            throw new IllegalArgumentException("El valor de la recompensa debe ser mayor a 0");
        }
        recompensa.setFechaInicio(LocalDateTime.now());
        recompensa.setFechaCierre(recompensa.getFechaInicio().plusWeeks(4));

        return recompensaRepository.save(recompensa);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void eliminarRecompensasExpiradas() {
        List<Recompensa> recompensasExpiradas = recompensaRepository.findAll().stream()
                .filter(recompensa -> recompensa.getFechaCierre().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());

        if (!recompensasExpiradas.isEmpty()) {
            recompensaRepository.deleteAll(recompensasExpiradas);
        }
    }

    public Page<Recompensa> getAllRecompensas(Pageable pageable) {
        return recompensaRepository.findAll(pageable);
    }
    public Recompensa getReferenceById(Long id) {
        return recompensaRepository.getReferenceById(id);
    }

    public void deleteRecompensaById(Long id) {
        recompensaRepository.deleteById(id);
    }
}
