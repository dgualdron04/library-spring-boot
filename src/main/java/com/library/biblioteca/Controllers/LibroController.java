package com.library.biblioteca.Controllers;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.biblioteca.Repository.EntitiesDTO.LibroDTO;
import com.library.biblioteca.Services.LibroService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;

@AllArgsConstructor
@RestController
@RequestMapping("/libros")
public class LibroController {
    
    @Autowired
    private LibroService libroService;

    @GetMapping("/")
    public List<LibroDTO> findAll() {
        return libroService.findAll();
    }

    @GetMapping("/disponibles")
    public List<LibroDTO> findAvailableBooks() {
        return libroService.findAvailableBooks();
    }
    

    @PostMapping("/")
    public ResponseEntity<Map<String, Object>> save(@Validated @RequestBody LibroDTO libroDTO, BindingResult result) {
        Map<String, Object> response = new HashMap<>();

        try {

            if (result.hasErrors()) {
                List<String> errors = result.getFieldErrors()
                        .stream()
                        .map(err -> "El campo " + err.getField() + " " + err.getDefaultMessage())
                        .collect(Collectors.toList());
                response.put("errors", errors);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            LibroDTO libroSave = null;
            libroSave = libroService.save(libroDTO);

            response.put("mensaje", "El libro ha sido creado con éxito");
            response.put("libro", libroSave);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar el guardado en la base de datos");
            response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
    

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable BigInteger id, @Validated @RequestBody LibroDTO libroDTO, BindingResult result) {

        Map<String, Object> response = new HashMap<>();

        try {

            if (result.hasErrors()) {
                List<String> errors = result.getFieldErrors()
                        .stream()
                        .map(err -> "El campo " + err.getField() + " " + err.getDefaultMessage())
                        .collect(Collectors.toList());
                response.put("errors", errors);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            LibroDTO libroUpdate = null;
            libroUpdate = libroService.update(id, libroDTO);

            response.put("mensaje", "El libro ha sido actualizado con éxito");
            response.put("libro", libroUpdate);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar el update en la base de datos");
            response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable BigInteger id){
        libroService.deleteById(id);
    } 

}