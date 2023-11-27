package io.bootify.gestion_bibliotecas.repos;

import io.bootify.gestion_bibliotecas.domain.LibroPrestado;
import io.bootify.gestion_bibliotecas.domain.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    Prestamo findFirstByLibroPrestado(LibroPrestado libroPrestado);

    boolean existsByLibroPrestadoId(Long id);

}
