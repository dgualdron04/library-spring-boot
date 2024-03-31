package com.library.biblioteca.Services.Impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.biblioteca.Configuration.PrestamoConversion;
import com.library.biblioteca.Repository.PrestamoRepository;
import com.library.biblioteca.Repository.UsuarioRepository;
import com.library.biblioteca.Repository.Entities.Prestamo;
import com.library.biblioteca.Repository.Entities.Usuario;
import com.library.biblioteca.Repository.EntitiesDTO.PrestamoDTO;
import com.library.biblioteca.Services.PrestamoService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PrestamoImplService implements PrestamoService {

    @Autowired
    private PrestamoConversion prestamoConversion;
    private PrestamoRepository prestamoRepository;
    private UsuarioRepository usuarioRepository;

    public PrestamoDTO save(PrestamoDTO prestamoDTO) {
        
        Prestamo prestamo = prestamoConversion.convertirDTOAPrestamo(prestamoDTO);
        prestamo.setFechaPrestamo(new Date());
        prestamoRepository.save(prestamo);
        return prestamoConversion.convertirPrestamoADTO(prestamo);

    }

    @Override
    public PrestamoDTO update(BigInteger id, PrestamoDTO prestamo){

        //busca el id del prestamo en la bd
        Optional<Prestamo> prestamoCurrentOptional = prestamoRepository.findById(id);

        //valida si el prestamo esta
        if(prestamoCurrentOptional.isPresent()){

            //convierto EL DTO que traia de parametro a entidad
            Prestamo prestamoCurrent = prestamoConversion.convertirDTOAPrestamo(prestamo);
            prestamoCurrent.setId(id);
            prestamoCurrent.setFechaPrestamo(prestamo.getFechaPrestamo());
            prestamoCurrent.setFechaDevolucion(prestamo.getFechaDevolucion());
            prestamoCurrent.setDevuelto(prestamo.getDevuelto());
            prestamoCurrent.setEstado(prestamo.getEstado());

            //Guardado en la BD
            prestamoRepository.save(prestamoCurrent);

            return prestamoConversion.convertirPrestamoADTO(prestamoCurrent);
        }
        return null;     
    }

    @Override 
    public void deleteById(BigInteger id){
        prestamoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoDTO> findAll(){
        List<Prestamo> prestamos = (List<Prestamo>) prestamoRepository.findAll();
        return prestamos.stream()
                        .map(prestamo -> prestamoConversion.convertirPrestamoADTO(prestamo))
                        .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoDTO> findAllPrestamoByUsuario(BigInteger idUsuario) {
        Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
        List<Prestamo> prestamos = (List<Prestamo>) prestamoRepository.findAllPrestamoByUsuario(usuario.get());
        return prestamos.stream().map(prestamo -> prestamoConversion.convertirPrestamoADTO(prestamo)).toList();
    }

    public PrestamoDTO cancelarPrestamo(BigInteger id) {
        //busca el id del prestamo en la bd
        Optional<Prestamo> prestamoCurrentOptional = prestamoRepository.findById(id);

        //valida si el prestamo esta
        if(prestamoCurrentOptional.isPresent()){

            //convierto EL DTO que traia de parametro a entidad
            Prestamo prestamoCurrent = prestamoCurrentOptional.get();
            prestamoCurrent.setId(id);
            prestamoCurrent.setEstado(false);

            //Guardado en la BD
            prestamoRepository.save(prestamoCurrent);

            return prestamoConversion.convertirPrestamoADTO(prestamoCurrent);
        }
        return null;  
    }

    public PrestamoDTO aprobarPrestamo(BigInteger id) {
        //busca el id del prestamo en la bd
        Optional<Prestamo> prestamoCurrentOptional = prestamoRepository.findById(id);

        //valida si el prestamo esta
        if(prestamoCurrentOptional.isPresent()){

            //convierto EL DTO que traia de parametro a entidad
            Prestamo prestamoCurrent = prestamoCurrentOptional.get();
            prestamoCurrent.setId(id);
            prestamoCurrent.setEstado(true);

            //Guardado en la BD
            prestamoRepository.save(prestamoCurrent);

            return prestamoConversion.convertirPrestamoADTO(prestamoCurrent);
        }
        return null;  
    }

    public PrestamoDTO devolverLibro(BigInteger id) {
        //busca el id del prestamo en la bd
        Optional<Prestamo> prestamoCurrentOptional = prestamoRepository.findById(id);

        //valida si el prestamo esta
        if(prestamoCurrentOptional.isPresent()){

            Date fecha = new Date();

            //convierto EL DTO que traia de parametro a entidad
            Prestamo prestamoCurrent = prestamoCurrentOptional.get();
            prestamoCurrent.setId(id);
            prestamoCurrent.setFechaDevolucion(fecha);
            prestamoCurrent.setDevuelto(true);

            //Guardado en la BD
            prestamoRepository.save(prestamoCurrent);

            return prestamoConversion.convertirPrestamoADTO(prestamoCurrent);
        }
        return null;
    }

}