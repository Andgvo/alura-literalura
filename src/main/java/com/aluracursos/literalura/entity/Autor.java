package com.aluracursos.literalura.entity;

import com.aluracursos.literalura.dto.AutorDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "autor")
public class Autor {

    @Id
    private String nombre;
    private Integer nacimiento;
    private Integer fallecimiento;
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL,  fetch = FetchType.LAZY)
    private List<Libro> libro;

    public Autor(AutorDto dto){
        nombre = dto.nombre();
        nacimiento = dto.nacimiento();
        fallecimiento = dto.fallecimiento();
    }

    @Override
    public String toString() {
        return "{" +
                "nombre=" + nombre +
                ", nacimiento='" + nacimiento + '\'' +
                ", fallecimiento=" + fallecimiento +
                "}";
    }
}
