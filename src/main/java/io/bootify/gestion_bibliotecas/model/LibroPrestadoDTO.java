package io.bootify.gestion_bibliotecas.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LibroPrestadoDTO {

    private Long id;

    private Long idLibro;

    private Long idLector;

    private Long idPrestamo;

    private Long libro;

    private Long lector;

    @NotNull
    private Long libros;

    @NotNull
    private Long lectores;

}
