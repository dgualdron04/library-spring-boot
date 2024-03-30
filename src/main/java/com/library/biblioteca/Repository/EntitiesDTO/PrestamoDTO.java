package com.library.biblioteca.Repository.EntitiesDTO;

import java.math.BigInteger;
import java.util.Date;

import com.library.biblioteca.Repository.Entities.Libro;
import com.library.biblioteca.Repository.Entities.Usuario;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PrestamoDTO {
    
    private BigInteger id;
    @NotNull(message = "no puede estar vacio")
    private Date fechaPrestamo; 
    private Date fechaDevolucion; 
    private Boolean estado;
    @NotNull(message = "no puede estar vacio")
    private Boolean devuelto;
    private Usuario usuario;
    private Libro libro;

}
