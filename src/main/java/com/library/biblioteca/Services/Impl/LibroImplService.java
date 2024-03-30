package com.library.biblioteca.Services.Impl;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.biblioteca.Configuration.LibroConversion;
import com.library.biblioteca.Repository.LibroRepository;
import com.library.biblioteca.Repository.Entities.Libro;
import com.library.biblioteca.Repository.EntitiesDTO.LibroDTO;
import com.library.biblioteca.Services.LibroService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LibroImplService implements LibroService {
    
    @Autowired
    private LibroRepository libroRepository;
    private LibroConversion libroConversion;

    @Override
    @Transactional
    public LibroDTO save(LibroDTO libroDTO) {

        Libro libro = libroConversion.convertirDTOALibro(libroDTO);
        libroRepository.save(libro);
        return libroConversion.convertirLibroADTO(libro);

    }

    @Override
    @Transactional
    public LibroDTO update(BigInteger id, LibroDTO libro){

        //busca el id del libro en la bd
        Optional<Libro> libroCurrentOptional = libroRepository.findById(id);

        //valida si el libro esta
        if(libroCurrentOptional.isPresent()){

            //convierto EL DTO que traia de parametro a entidad
            Libro libroCurrent = libroConversion.convertirDTOALibro(libro);
            libroCurrent.setId(id);
            libroCurrent.setTitulo(libro.getTitulo());
            libroCurrent.setAutor(libro.getAutor());
            libroCurrent.setGenero(libro.getGenero());
            libroCurrent.setAnioPublicacion(libro.getAnioPublicacion());
            libroCurrent.setInventario(libro.getInventario());

            //Guardado en la BD
            libroRepository.save(libroCurrent);

            return libroConversion.convertirLibroADTO(libroCurrent);
        }
        return null;     
    }

    @Override 
    public void deleteById(BigInteger id){
        libroRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LibroDTO> findAll(){
        List<Libro> libros = (List<Libro>) libroRepository.findAll();
        return libros.stream()
                        .map(libro -> libroConversion.convertirLibroADTO(libro))
                        .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LibroDTO> findAvailableBooks(){
        List<Libro> libros = (List<Libro>) libroRepository.findAvailableBooks();
        return libros.stream()
                        .map(libro -> libroConversion.convertirLibroADTO(libro))
                        .toList();
    }

}