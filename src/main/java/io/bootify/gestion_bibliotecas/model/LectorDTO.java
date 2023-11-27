package io.bootify.gestion_bibliotecas.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LectorDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String nombre;

    @NotNull
    @Size(max = 255)
    private String apellido;

    @Size(max = 255)
    private String libro;

    @NotNull
    @Size(max = 255)
    private String correo;

}
