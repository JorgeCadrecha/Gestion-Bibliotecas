package io.bootify.gestion_bibliotecas.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PrestamoDTO {

    private Long id;

    @Size(max = 255)
    private String fechaDePrestamo;

    @Size(max = 255)
    private String fechaDeDevolucion;

    @NotNull
    @Size(max = 255)
    private String estado;

    private Long libroPrestado;

}
