package com.library.biblioteca.Configuration;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.library.biblioteca.Repository.Entities.Usuario;
import com.library.biblioteca.Repository.EntitiesDTO.UsuarioDTO;

@Component
public class UsuarioConversion {
    
    @Autowired
    private ModelMapper dbm;

    public Usuario convertirDTOAUsuario(UsuarioDTO usuarioDTO){
        return dbm.map(usuarioDTO,Usuario.class);  
    }

    public UsuarioDTO convertirUsuarioADTO(Usuario usuario){
        UsuarioDTO usuarioDTO = dbm.map(usuario, UsuarioDTO.class);
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setCedula(usuario.getCedula());

        return usuarioDTO;
    }
}