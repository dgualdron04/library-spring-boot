package com.library.biblioteca.Repository.EntitiesDTO;

import java.math.BigInteger;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UsuarioDTO {

    private BigInteger id;
    @NotNull(message = "no puede estar vacio")
    private Long cedula;
    
}