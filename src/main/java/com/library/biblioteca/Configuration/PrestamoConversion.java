package com.library.biblioteca.Configuration;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.library.biblioteca.Repository.Entities.Prestamo;
import com.library.biblioteca.Repository.EntitiesDTO.PrestamoDTO;

@Component
public class PrestamoConversion {
    
    @Autowired
    private ModelMapper dbm;

    public Prestamo convertirDTOAPrestamo(PrestamoDTO prestamoDTO){
        return dbm.map(prestamoDTO,Prestamo.class);  
    }

    public PrestamoDTO convertirPrestamoADTO(Prestamo prestamo){
        PrestamoDTO prestamoDTO = dbm.map(prestamo, PrestamoDTO.class);
        prestamoDTO.setId(prestamo.getId());
        prestamoDTO.setFechaPrestamo(prestamo.getFechaPrestamo());
        prestamoDTO.setFechaDevolucion(prestamo.getFechaDevolucion());
        prestamoDTO.setDevuelto(prestamo.getDevuelto());
        prestamoDTO.setEstado(prestamo.getEstado());
        prestamoDTO.setUsuario(prestamoDTO.getUsuario());
        prestamoDTO.setLibro(prestamo.getLibro());
        return prestamoDTO;
    }
}