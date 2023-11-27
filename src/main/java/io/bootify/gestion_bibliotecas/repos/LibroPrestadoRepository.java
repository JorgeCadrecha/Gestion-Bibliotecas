package io.bootify.gestion_bibliotecas.repos;

import io.bootify.gestion_bibliotecas.domain.Lector;
import io.bootify.gestion_bibliotecas.domain.Libro;
import io.bootify.gestion_bibliotecas.domain.LibroPrestado;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LibroPrestadoRepository extends JpaRepository<LibroPrestado, Long> {

    LibroPrestado findFirstByLibro(Libro libro);

    LibroPrestado findFirstByLector(Lector lector);

    LibroPrestado findFirstByLibros(Libro libro);

    LibroPrestado findFirstByLectores(Lector lector);

}
