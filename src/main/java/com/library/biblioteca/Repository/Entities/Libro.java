package com.library.biblioteca.Repository.Entities;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
//el titulo no puede ser repetido
@Table(name = "libro")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(name = "titulo" , nullable = false, unique = true)
    private String titulo;

    @Column(name = "autor" , nullable = false)
    private String autor;

    @Column(name = "genero" , nullable = false)
    private String genero;

    @Column(name = "anioPublicacion" , nullable = false)
    private Integer anioPublicacion;

    @Column(name = "inventario" , nullable = false)
    private Long inventario;

    @Column(name="create_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createAt; 
}