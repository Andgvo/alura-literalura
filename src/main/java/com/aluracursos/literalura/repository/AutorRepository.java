package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.entity.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, String> {

    @Query("select a from Autor a " +
            "where :anio between a.nacimiento and a.fallecimiento ")
    List<Autor> findAllVivoByAnio(Integer anio);

}
