package com.upao.recicla.domain.entity;

import com.upao.recicla.domain.dto.residuoDto.DatosActualizarResiduo;
import com.upao.recicla.domain.dto.residuoDto.DatosRegistroResiduo;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "residuos")
@Entity(name = "Residuo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Residuo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String nombre;
    @Column(length = 100)
    private String descripcion;
    @Column(nullable = false, length = 100)
    private String tipo;
    @Column(nullable = false)
    private Double puntos;

    public Residuo(Long residuo_id){
        this.id = residuo_id;
    }

    public Residuo(DatosRegistroResiduo datosRegistroResiduo){
        this.nombre = datosRegistroResiduo.nombre();
        this.descripcion = datosRegistroResiduo.descripcion();
        this.tipo = datosRegistroResiduo.tipo();
        this.puntos = datosRegistroResiduo.puntos();
    }

    public void actualizarResiduo(DatosActualizarResiduo datosActualizarResiduo){
        if (datosActualizarResiduo.nombre() != null){
            this.nombre = datosActualizarResiduo.nombre();
        }
        if (datosActualizarResiduo.tipo() != null){
            this.tipo = datosActualizarResiduo.tipo();
        }
        if (datosActualizarResiduo.descripcion() != null){
            this.descripcion = datosActualizarResiduo.descripcion();
        }
    }
}
