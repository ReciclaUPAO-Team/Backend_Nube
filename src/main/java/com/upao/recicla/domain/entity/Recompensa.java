package com.upao.recicla.domain.entity;

import com.upao.recicla.domain.dto.recompensaDto.DatosActualizarRecompensa;
import com.upao.recicla.domain.dto.recompensaDto.DatosRegistroRecompensa;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "recompensas")
@Entity(name = "Recompensa")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Recompensa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    @Column(name = "puntos_requeridos")
    private Double valor;
    @Column(name = "fecha_inicio", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false)
    private LocalDateTime fechaInicio;
    @Column(name = "fecha_cierre")
    private LocalDateTime fechaCierre;
    @Column(name = "imagen_path", nullable = false, columnDefinition = "LONGTEXT")
    private String imagenPath;

    public Recompensa(DatosRegistroRecompensa datos){
        this.titulo = datos.titulo();
        this.descripcion = datos.descripcion();
        this.categoria = datos.categoria();
        this.valor = datos.valor();
    }

    public void actualizarRecompensa(DatosActualizarRecompensa datosActualizarRecompensa){
        if (datosActualizarRecompensa.titulo() != null){
            this.titulo = datosActualizarRecompensa.titulo();
        }
        if(datosActualizarRecompensa.categoria() != null){
            this.categoria = datosActualizarRecompensa.categoria();
        }
        if(datosActualizarRecompensa.valor() != null){
            this.valor = datosActualizarRecompensa.valor();
        }
    }
}
