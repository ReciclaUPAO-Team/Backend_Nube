package com.upao.recicla.controller;

import com.upao.recicla.domain.entity.Canje;
import com.upao.recicla.domain.service.CanjeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/canje")
@RequiredArgsConstructor
public class CanjeController {

    @Autowired
    private final CanjeService canjeService;

    @PostMapping("/canjear")
    @Transactional
    public ResponseEntity<?> canjearRecompensa(@RequestParam("nombreRecompensa") String nombreRecompensa) {
        try {
            canjeService.canjearPuntos(nombreRecompensa);
            return ResponseEntity.ok().body("Recompensa canjeada con éxito");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}

