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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.biblioteca.Repository.EntitiesDTO.PrestamoDTO;
import com.library.biblioteca.Services.PrestamoService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/prestamos")
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;

    @GetMapping("/")
    public List<PrestamoDTO> findAll() {
        return prestamoService.findAll();
    }

    @GetMapping("/{id}")
    public List<PrestamoDTO> findAllPrestamoByUsuario(@PathVariable BigInteger id) {
        return prestamoService.findAllPrestamoByUsuario(id);
    }

    @PostMapping("/")
    public ResponseEntity<Map<String, Object>> save(@Validated @RequestBody PrestamoDTO prestamoDTO, BindingResult result) {
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

            PrestamoDTO prestamoSave = null;
            prestamoSave = prestamoService.save(prestamoDTO);

            response.put("mensaje", "El prestamo ha sido creado con éxito");
            response.put("usuario", prestamoSave);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar el guardado en la base de datos");
            response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @PutMapping("/cancelar/{id}")
    public ResponseEntity<Map<String, Object>> cancelarPrestamo(@PathVariable BigInteger id) {

        Map<String, Object> response = new HashMap<>();

        try {

            PrestamoDTO prestamoCancelar = null;
            prestamoCancelar = prestamoService.cancelarPrestamo(id);

            response.put("mensaje", "El prestamo ha sido cancelado con éxito");
            response.put("prestamo", prestamoCancelar);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar el cancelar prestamo en la base de datos");
            response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @PutMapping("/aprobar/{id}")
    public ResponseEntity<Map<String, Object>> aprobarPrestamo(@PathVariable BigInteger id) {

        Map<String, Object> response = new HashMap<>();

        try {

            PrestamoDTO prestamoAprobar = null;
            prestamoAprobar = prestamoService.aprobarPrestamo(id);

            response.put("mensaje", "El prestamo ha sido aprobado con éxito");
            response.put("prestamo", prestamoAprobar);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar el cancelar prestamo en la base de datos");
            response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @PutMapping("/devolver/{id}")
    public ResponseEntity<Map<String, Object>> devolverPrestamo(@PathVariable BigInteger id) {

        Map<String, Object> response = new HashMap<>();

        try {

            PrestamoDTO prestamoDevolver = null;
            prestamoDevolver = prestamoService.devolverLibro(id);

            response.put("mensaje", "El prestamo ha sido devuelto con éxito");
            response.put("prestamo", prestamoDevolver);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar el cancelar prestamo en la base de datos");
            response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
}
