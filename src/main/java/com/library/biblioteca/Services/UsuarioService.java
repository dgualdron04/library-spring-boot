package com.library.biblioteca.Services;

import java.math.BigInteger;
import java.util.List;

import com.library.biblioteca.Repository.EntitiesDTO.UsuarioDTO;

public interface UsuarioService {

    public UsuarioDTO save(UsuarioDTO usuario);

    public UsuarioDTO update(BigInteger id, UsuarioDTO usuario);

    void deleteById(BigInteger id);

    List<UsuarioDTO> findAll();

}