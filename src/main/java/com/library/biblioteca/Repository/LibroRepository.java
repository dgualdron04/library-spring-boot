package com.library.biblioteca.Repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.library.biblioteca.Repository.Entities.Libro;

@Repository
public interface LibroRepository extends JpaRepository<Libro, BigInteger> {
    String AVAILABLE_BOOKS_QUERY = "SELECT l.* FROM libro l LEFT JOIN prestamo p ON p.libro_id = l.id HAVING COUNT((SELECT li.id FROM libro li LEFT JOIN prestamo pre ON pre.libro_id = li.id WHERE pre.estado = true AND (pre.devuelto = false OR pre.devuelto is null))) < l.inventario";
    
    @Query(value = AVAILABLE_BOOKS_QUERY, nativeQuery = true)
    List<Libro> findAvailableBooks();
}
