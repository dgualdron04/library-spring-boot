package com.library.biblioteca.Repository.Entities;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "prestamo")
public class Prestamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(nullable = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fechaPrestamo; 

    @Column(nullable = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fechaDevolucion; 
    
    @Column(nullable = false)
    private Boolean devuelto = false;

    @Column(nullable = true)
    private Boolean estado = null;

    @ManyToOne(fetch = FetchType.EAGER)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    private Libro libro;

    @Column(name="create_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createAt; 
}