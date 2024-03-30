package com.library.biblioteca.Services;

import java.math.BigInteger;
import java.util.List;

import com.library.biblioteca.Repository.EntitiesDTO.PrestamoDTO;

public interface PrestamoService {
    
    public PrestamoDTO save(PrestamoDTO prestamo);

    public PrestamoDTO update(BigInteger id, PrestamoDTO libro);

    void deleteById(BigInteger id);

    List<PrestamoDTO> findAll();

    List<PrestamoDTO> findAllPrestamoByUsuario(BigInteger idUsuario);

    public PrestamoDTO cancelarPrestamo(BigInteger id);

    public PrestamoDTO aprobarPrestamo(BigInteger id);

    public PrestamoDTO devolverLibro(BigInteger id);

}