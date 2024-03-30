package com.library.biblioteca.Repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.library.biblioteca.Repository.Entities.Prestamo;
import com.library.biblioteca.Repository.Entities.Usuario;

@Repository
public interface PrestamoRepository  extends JpaRepository<Prestamo, BigInteger> {
    List<Prestamo> findAllPrestamoByUsuario(Usuario usuario);
}
