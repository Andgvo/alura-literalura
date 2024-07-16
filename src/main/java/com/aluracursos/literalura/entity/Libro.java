package com.aluracursos.literalura.entity;

import com.aluracursos.literalura.dto.LibroDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "libro")
public class Libro {

    @Id
    private Long id;
    @Column(length = 1000)
    private String titulo;
    private String idioma;
    private Integer numeroDescargas;
    @ManyToOne
    private Autor autor;

    public Libro(LibroDto dto) {
        id = dto.id();
        titulo = dto.titulo();
        idioma = dto.idiomas().get(0);
        numeroDescargas = dto.numeroDescargas();
        if (!dto.autores().isEmpty()) {
            autor = new Autor(dto.autores().get(0));
        }
    }

    @Override
    public String toString() {
        return
                "  id=" + id +
                ", titulo='" + titulo + '\'' +
                ", idioma=" + idioma +
                ", numeroDescargas=" + numeroDescargas +
                ", autor=" + autor;
    }
}
