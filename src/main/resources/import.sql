INSERT INTO usuario (cedula, email, pwd, create_at) VALUES (1005372613, 'account@debuggeandoieas.com', 'to_be_encoded', '2015-12-01');
INSERT INTO usuario (cedula, email, pwd, create_at) VALUES (1005372614, 'account2@debuggeandoieas.com', 'to_be_encoded_user', '2015-12-01');
INSERT INTO usuario (cedula, email, pwd, create_at) VALUES (1005372615, 'account3@debuggeandoieas.com', 'to_be_encoded_bibliotecario', '2015-12-01');

INSERT INTO libro (titulo, autor, genero, anio_publicacion, inventario, create_at) VALUES ('El Cielo y el Infierno', 'Allan Kardec', 'Filosofía', '2011', 11, '2015-12-01');

INSERT INTO prestamo (fecha_prestamo, fecha_devolucion, libro_id, usuario_id, devuelto, create_at) VALUES ('2024-03-25', null, 1, 1, 0, '2015-12-01');


INSERT INTO roles (role_name, description) VALUES ('ROLE_ADMIN', 'cant view account endpoint'); 
INSERT INTO roles (role_name, description) VALUES ('ROLE_USER', 'cant view account endpoint'); 
INSERT INTO roles (role_name, description) VALUES ('ROLE_BIBLIOTECARIO', 'cant view account endpoint'); 

INSERT INTO usuario_roles (roles_id, usuario_id) VALUES (1, 1); 
INSERT INTO usuario_roles (roles_id, usuario_id) VALUES (2, 2); 
INSERT INTO usuario_roles (roles_id, usuario_id) VALUES (3, 3); 