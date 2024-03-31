package com.library.biblioteca.Repository.EntitiesDTO;

import java.math.BigInteger;
import java.util.List;

import com.library.biblioteca.Repository.Entities.Role;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UsuarioDTO {

    private BigInteger id;
    @NotNull(message = "no puede estar vacio")
    private Long cedula;
    private String email;
    private List<Role> roles;
    
}