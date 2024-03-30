package com.library.biblioteca.Services;

import java.math.BigInteger;
import java.util.List;

import com.library.biblioteca.Repository.EntitiesDTO.LibroDTO;

public interface LibroService {
    
    public LibroDTO save(LibroDTO libro);

    public LibroDTO update(BigInteger id, LibroDTO libro);

    void deleteById(BigInteger id);

    List<LibroDTO> findAll();

    List<LibroDTO> findAvailableBooks();
}