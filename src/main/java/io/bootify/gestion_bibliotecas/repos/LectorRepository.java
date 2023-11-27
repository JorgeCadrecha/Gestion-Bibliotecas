package io.bootify.gestion_bibliotecas.repos;

import io.bootify.gestion_bibliotecas.domain.Lector;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LectorRepository extends JpaRepository<Lector, Long> {
}
