package com.library.biblioteca.Configuration;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.library.biblioteca.Repository.Entities.Libro;
import com.library.biblioteca.Repository.EntitiesDTO.LibroDTO;

@Component
public class LibroConversion {
    
    @Autowired
    private ModelMapper dbm;

    public Libro convertirDTOALibro(LibroDTO libroDTO){
        return dbm.map(libroDTO,Libro.class);  
    }

    public LibroDTO convertirLibroADTO(Libro libro){
        LibroDTO libroDTO = dbm.map(libro, LibroDTO.class);
        libroDTO.setId(libro.getId());
        libroDTO.setTitulo(libro.getTitulo());
        libroDTO.setAutor(libro.getAutor());
        libroDTO.setGenero(libro.getGenero());
        libroDTO.setAnioPublicacion(libro.getAnioPublicacion());
        libroDTO.setInventario(libro.getInventario());

        return libroDTO;
    }
}