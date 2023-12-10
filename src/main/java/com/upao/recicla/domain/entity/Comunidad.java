package com.upao.recicla.domain.entity;

import com.upao.recicla.domain.dto.comunidadDto.DatosActualizarComunidad;
import com.upao.recicla.domain.dto.comunidadDto.DatosRegistroComunidad;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Table(name = "comunidades")
@Entity(name = "Comunidad")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Comunidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "fecha_creacion", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false)
    private String fechaCreacion;
    @Column(nullable = false, length = 155, unique = true)
    private String nombre;
    @Column(length = 255)
    private String descripcion;
    @ManyToMany(mappedBy = "comunidades")
    private List<Usuario> usuarios;

    public Comunidad(DatosRegistroComunidad datos) {
        this.nombre = datos.nombre();
        this.descripcion = datos.descripcion();
    }

    public void actualizarComunidad(DatosActualizarComunidad datosActualizarComunidad){
        if(datosActualizarComunidad.nombre() != null){
            this.nombre = datosActualizarComunidad.nombre();
        }
        if(datosActualizarComunidad.descripcion() != null){
            this.descripcion = datosActualizarComunidad.descripcion();
        }
    }
}
