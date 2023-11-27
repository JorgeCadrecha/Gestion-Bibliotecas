package io.bootify.gestion_bibliotecas.repos;

import io.bootify.gestion_bibliotecas.domain.Libro;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LibroRepository extends JpaRepository<Libro, Long> {
}
