package com.library.biblioteca.Repository.EntitiesDTO;

import java.math.BigInteger;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LibroDTO {

    private BigInteger id;
    @NotEmpty(message = "no puede estar vacio")
    private String titulo;
    @NotEmpty(message = "no puede estar vacio")
    private String autor;
    @NotEmpty(message = "no puede estar vacio")
    private String genero;
    @NotNull(message = "no puede estar vacio")
    private Integer anioPublicacion;
    @NotNull(message = "no puede estar vacio")
    private Long inventario;
    
}
