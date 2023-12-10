package com.upao.recicla.domain.entity;

public enum NivelUsuario {
    PLATA(0), ORO(60), PLATINO(120), DIAMANTE(180);
    private final double puntosMinimos;
    NivelUsuario(double puntosMinimos) {
        this.puntosMinimos = puntosMinimos;
    }
    public static NivelUsuario determinarNivel(double puntos, NivelUsuario nivelActual) {
        NivelUsuario nivelAsignado = PLATA;
        for (NivelUsuario nivel : NivelUsuario.values()) {
            if (puntos >= nivel.puntosMinimos) {
                nivelAsignado = nivel;
            }
        }
        return nivelAsignado.compareTo(nivelActual) > 0 ? nivelAsignado : nivelActual;
    }
}

