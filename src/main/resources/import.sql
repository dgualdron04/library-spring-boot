INSERT INTO usuario (cedula, create_at) VALUES (1005372613, '2015-12-01');

INSERT INTO libro (titulo, autor, genero, anio_publicacion, inventario, create_at) VALUES ('El Cielo y el Infierno', 'Allan Kardec', 'Filosofía', '2011', 11, '2015-12-01');

INSERT INTO prestamo (fecha_prestamo, fecha_devolucion, libro_id, usuario_id, devuelto, create_at) VALUES ('2024-03-25', null, 1, 1, 0, '2015-12-01');