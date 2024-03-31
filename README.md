# Como hacer un API RESTful de Biblioteca en SPRING BOOT sin morir en el intento
La idea es elaborar una API RESTful usando SPRING BOOT, aplicando DTO para el intercambio seguro de datos entre el cliente y el servidor, además de aplicar JWT para la autenticación.

#### El enunciado que vamos a abordar es el siguiente
Se requiere desarrollar un sistema de gestión de una biblioteca utilizando Spring Boot y creando una API RESTful. El sistema debe permitir a los usuarios realizar diferentes acciones según sus roles, utilizando autenticación JWT con roles asignados. Los roles disponibles serán: Administrador, Bibliotecario y Usuario.

Los usuarios administradores tienen acceso completo al sistema, incluyendo la gestión de libros, usuarios y préstamos. Los bibliotecarios pueden gestionar préstamos y libros, pero no tienen permiso para modificar usuarios ni acceder a información confidencial. Los usuarios normales solo pueden consultar libros y solicitar préstamos.

Cada libro de la biblioteca tiene un título, autor, género, año de publicación y una cantidad disponible en inventario. Los préstamos registran la información del usuario que realiza el préstamo, el libro solicitado, la fecha de préstamo y la fecha de devolución prevista.

Se espera que el sistema permita las siguientes operaciones:

1. Registro de usuarios con roles asignados.
2. Inicio de sesión con autenticación JWT.
3. Gestión de libros: creación, actualización, eliminación y consulta de libros.
4. Gestión de préstamos: solicitud, aprobación, devolución y cancelación de préstamos.
5. Consulta de libros disponibles y préstamos activos por parte de los usuarios.

El sistema debe implementar la transferencia de datos utilizando DTO (Data Transfer Object) para garantizar una comunicación eficiente entre el cliente y el servidor, así como para mejorar la seguridad de la aplicación.

Resultado Esperado:

Se espera que el sistema desarrollado permita a los usuarios registrarse, iniciar sesión y realizar las operaciones correspondientes según sus roles asignados. Los administradores deberían tener acceso completo a la gestión de libros, usuarios y préstamos, mientras que los bibliotecarios pueden gestionar préstamos y libros. Los usuarios normales solo pueden consultar libros y solicitar préstamos.

La API RESTful debe proporcionar endpoints claramente definidos para cada una de las operaciones mencionadas anteriormente. Se espera que la autenticación JWT garantice la seguridad de las rutas protegidas y que se utilicen DTO para el intercambio seguro de datos entre el cliente y el servidor.

Además, se espera que el sistema sea robusto, seguro y escalable, y que cumpla con los principios de diseño de API RESTful.

## Ahora seguiremos el paso a paso de como desarrollar este proyecto.

1. **Sacar los requisitos separandolos por roles.**
    <br>
    * **Requisitos Generales**
        - Registrar usuarios
        - Iniciar Sesión
        <br>
    * **Rol Administrador**
        - **Modulo de Usuarios**
            - Crear usuarios
            - Listar usuarios
            - Editar usuarios
            - Eliminar usuarios
        - **Modulo de Libros**
            - Crear libros
            - Editar libros
            - Listar libros
            - Eliminar Libros
            - Listar Libros Disponibles
        - **Modulo de Prestamos**
            - Solicitar Prestamo
            - Aprobar Prestamo
            - Marcar Devolución
            - Cancelar Prestamo
            - Listar Prestamos
            - Listar Prestamos de un Usuario
            <br>
    * **Rol Bibliotecario**
        - **Modulo de Libros**
            - Crear libros
            - Listar libros
            - Editar libros
            - Eliminar Libros
            - Listar Libros Disponibles
            <br>
        - **Modulo de Prestamos**
            - Aprobar prestamo
            - Marcar Devolución
            - Cancelar Prestamo
            - Listar Prestamos
            <br>
    * **Rol de Usuario**
        - **Modulo de Libros**
            - Listar libros disponibles
        - **Modulo de Prestamos**
            - Solicitar prestamo
            - Devolver prestamo
            - Listar mis prestamos
            <br>
    * **Excepciones**
        - No se puede eliminar un usuario con al menos un prestamo.
        - No se puede eliminar un libro con al menos un prestamo.
        - No se puede prestar un libro si no hay stock.
        - No se puede aprobar un libro si no hay stock.
        - El titulo no puede ser repetido.
        - La cedula del usuario es unica.

2. **Creamos un esquema de la BD**
Realmente no se especifican los datos del usuario así que simplemente le vamos a poner el campo de cedula.
![](https://lh3.googleusercontent.com/u/0/drive-viewer/AKGpihbM4bZ_aYu_eJNt0-nnpdKjtBt4wbcpA1OiK0HCnOLZfhK34pbvvj1ZLqZ7c9-feMK30JgAqe4uneP-sYSfn3u2MgFIPw=w1920-h878)

3. **Creamos el proyecto en SPRING BOOT**
![](https://lh3.googleusercontent.com/u/0/drive-viewer/AKGpihaNoKU7saj0XnEX5jTAKyomwJrahJSlPL5rm2Yf1jU2QiQjCO9Y1pLwPAowUnECrB14tt7YuIjCwhg9g0CsihCgNxeyNg=w1920-h878-v0)

4. **Creamos la estructura de nuestro proyecto**
En esta estructura aún no se agrega la seguridad (Spring Boot Security) ni la autenticación por JWT.
    ``` 
    -Biblioteca
    -Configuration
    -Controllers
    -Exceptions
    -Repository
    -Entities
    -EntitiesDTO
    -Services
    -Impl
    -Utils
    ```

5. **Creamos nuestras Entidades**
    - **Libro.java**
    ```
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
    ```
    - **Usuario.java**
    ```
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
    @Table(name = "usuario")
    public class Usuario {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private BigInteger id;

        @Column(nullable = false,unique = true)
        private Long cedula;

        @Column(name="create_at")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date createAt; 
    }
    ```
    - **Prestamo.java**
    ```
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

        @Column(nullable = false)
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
    ```

6. **Creamos las entidades pero en DTO**
    - **LibroDTO.java**
    ```
    package com.library.biblioteca.Repository.EntitiesDTO;

    import java.math.BigInteger;

    import jakarta.validation.constraints.NotEmpty;
    import jakarta.validation.constraints.NotNull;
    import lombok.Data;

    @Data
    public class LibroDTO {

        private BigInteger id;
        @NotEmpty(message = "no puede estar vacio")
        private String titulo;
        @NotEmpty(message = "no puede estar vacio")
        private String autor;
        @NotEmpty(message = "no puede estar vacio")
        private String genero;
        @NotNull(message = "no puede estar vacio")
        private Integer anioPublicacion;
        @NotNull(message = "no puede estar vacio")
        private Long inventario;
        
    }
    ```
    - **UsuarioDTO.java**
    ```
    package com.library.biblioteca.Repository.EntitiesDTO;

    import java.math.BigInteger;

    import jakarta.validation.constraints.NotNull;
    import lombok.Data;

    @Data
    public class UsuarioDTO {

        private BigInteger id;
        @NotNull(message = "no puede estar vacio")
        private Long cedula;
        
    }
    ```
    - **PrestamoDTO.java**
    ```
    package com.library.biblioteca.Repository.EntitiesDTO;

    import java.math.BigInteger;
    import java.util.Date;

    import com.library.biblioteca.Repository.Entities.Libro;
    import com.library.biblioteca.Repository.Entities.Usuario;

    import jakarta.validation.constraints.NotNull;
    import lombok.Data;

    @Data
    public class PrestamoDTO {
        
        private BigInteger id;
        @NotNull(message = "no puede estar vacio")
        private Date fechaPrestamo; 
        private Date fechaDevolucion; 
        private Boolean estado;
        @NotNull(message = "no puede estar vacio")
        private Boolean devuelto;
        private Usuario usuario;
        private Libro libro;

    }
    ```

7. **Creamos los Repositorios**
    - **LibroRepository.java**
    ```
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
    ```
    - **UsuarioRepository.java**
    ```
    package com.library.biblioteca.Repository;

    import java.math.BigInteger;

    import org.springframework.data.jpa.repository.JpaRepository;

    import com.library.biblioteca.Repository.Entities.Usuario;

    public interface UsuarioRepository extends JpaRepository<Usuario, BigInteger> {
        
    }
    ```
    - **PrestamoRepository**
    ```
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
    ```

8. **Creamos los Servicios**
    - **LibroService.java**
    ```
    package com.library.biblioteca.Services;

    import java.math.BigInteger;
    import java.util.List;

    import com.library.biblioteca.Repository.EntitiesDTO.LibroDTO;

    public interface LibroService {
        
        public LibroDTO save(LibroDTO libro);

        public LibroDTO update(BigInteger id, LibroDTO libro);

        void deleteById(BigInteger id);

        List<LibroDTO> findAll();

        List<LibroDTO> findAvailableBooks();
    }
    ```
    - **UsuarioService.java**
    ```
    package com.library.biblioteca.Services;

    import java.math.BigInteger;
    import java.util.List;

    import com.library.biblioteca.Repository.EntitiesDTO.UsuarioDTO;

    public interface UsuarioService {

        public UsuarioDTO save(UsuarioDTO usuario);

        public UsuarioDTO update(BigInteger id, UsuarioDTO usuario);

        void deleteById(BigInteger id);

        List<UsuarioDTO> findAll();

    }
    ```
    - **PrestamoService.java**
    ```
    package com.library.biblioteca.Services;

    import java.math.BigInteger;
    import java.util.List;

    import com.library.biblioteca.Repository.EntitiesDTO.PrestamoDTO;

    public interface PrestamoService {
        
        public PrestamoDTO save(PrestamoDTO prestamo);

        public PrestamoDTO update(BigInteger id, PrestamoDTO libro);

        void deleteById(BigInteger id);

        List<PrestamoDTO> findAll();

        List<PrestamoDTO> findAllPrestamoByUsuario(BigInteger idUsuario);

        public PrestamoDTO cancelarPrestamo(BigInteger id);

        public PrestamoDTO aprobarPrestamo(BigInteger id);

        public PrestamoDTO devolverLibro(BigInteger id);

    }
    ```

9. **Creamos el ModelMapper y las conversiones**
Agrega la dependencia modelmapper en el pom.xml
    - **pom.xml**
    ```
    <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
        <modelVersion>4.0.0</modelVersion>
        <parent>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-parent</artifactId>
            <version>3.2.4</version>
            <relativePath/> <!-- lookup parent from repository -->
        </parent>
        <groupId>com.example</groupId>
        <artifactId>demo.JWT</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <name>demo.JWT</name>
        <description>Demo project for Spring Boot</description>
        <properties>
            <java.version>17</java.version>
        </properties>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-data-jpa</artifactId>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-validation</artifactId>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
            </dependency>

            <dependency>
                <groupId>org.modelmapper</groupId>
                <artifactId>modelmapper</artifactId>
                <version>3.1.1</version>
            </dependency>

            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-devtools</artifactId>
                <scope>runtime</scope>
                <optional>true</optional>
            </dependency>
            <dependency>
                <groupId>com.h2database</groupId>
                <artifactId>h2</artifactId>
                <scope>runtime</scope>
            </dependency>
            <dependency>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <optional>true</optional>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-test</artifactId>
                <scope>test</scope>
            </dependency>
            <dependency>
                <groupId>org.springframework.security</groupId>
                <artifactId>spring-security-test</artifactId>
                <scope>test</scope>
            </dependency>
        </dependencies>

        <build>
            <plugins>
                <plugin>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-maven-plugin</artifactId>
                    <configuration>
                        <excludes>
                            <exclude>
                                <groupId>org.projectlombok</groupId>
                                <artifactId>lombok</artifactId>
                            </exclude>
                        </excludes>
                    </configuration>
                </plugin>
            </plugins>
        </build>

    </project>
    ```
    
    - **ModelMapperConfig.java**
    ```
    package com.example.demo.JWT.Biblioteca.Configuration;

    import org.modelmapper.ModelMapper;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;

    @Configuration
    public class ModelMapperConfig {
        
        @Bean
        public ModelMapper modelMapper(){        
            return new ModelMapper();
        }
    }
    ```

    - **LibroConversion.java**
    ```
    package com.library.biblioteca.Configuration;

    import org.modelmapper.ModelMapper;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Component;

    import com.library.biblioteca.Repository.Entities.Libro;
    import com.library.biblioteca.Repository.EntitiesDTO.LibroDTO;

    @Component
    public class LibroConversion {
        
        @Autowired
        private ModelMapper dbm;

        public Libro convertirDTOALibro(LibroDTO libroDTO){
            return dbm.map(libroDTO,Libro.class);  
        }

        public LibroDTO convertirLibroADTO(Libro libro){
            LibroDTO libroDTO = dbm.map(libro, LibroDTO.class);
            libroDTO.setId(libro.getId());
            libroDTO.setTitulo(libro.getTitulo());
            libroDTO.setAutor(libro.getAutor());
            libroDTO.setGenero(libro.getGenero());
            libroDTO.setAnioPublicacion(libro.getAnioPublicacion());
            libroDTO.setInventario(libro.getInventario());

            return libroDTO;
        }
    }
    ```

    - **UsuarioConversion.java**
    ```
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
    ```

    - **PrestamoConversion.java**
    ```
    package com.library.biblioteca.Configuration;

    import org.modelmapper.ModelMapper;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Component;

    import com.library.biblioteca.Repository.Entities.Prestamo;
    import com.library.biblioteca.Repository.EntitiesDTO.PrestamoDTO;

    @Component
    public class PrestamoConversion {
        
        @Autowired
        private ModelMapper dbm;

        public Prestamo convertirDTOAPrestamo(PrestamoDTO prestamoDTO){
            return dbm.map(prestamoDTO,Prestamo.class);  
        }

        public PrestamoDTO convertirPrestamoADTO(Prestamo prestamo){
            PrestamoDTO prestamoDTO = dbm.map(prestamo, PrestamoDTO.class);
            prestamoDTO.setId(prestamo.getId());
            prestamoDTO.setFechaPrestamo(prestamo.getFechaPrestamo());
            prestamoDTO.setFechaDevolucion(prestamo.getFechaDevolucion());
            prestamoDTO.setDevuelto(prestamo.getDevuelto());
            prestamoDTO.setEstado(prestamo.getEstado());
            prestamoDTO.setUsuario(prestamoDTO.getUsuario());
            prestamoDTO.setLibro(prestamo.getLibro());
            return prestamoDTO;
        }
    }
    ```

10. **Creamos la implementación de los Servicios**
    - **LibroImplService.java**
    ```
    package com.library.biblioteca.Services.Impl;

    import java.math.BigInteger;
    import java.util.List;
    import java.util.Optional;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import com.library.biblioteca.Configuration.LibroConversion;
    import com.library.biblioteca.Repository.LibroRepository;
    import com.library.biblioteca.Repository.Entities.Libro;
    import com.library.biblioteca.Repository.EntitiesDTO.LibroDTO;
    import com.library.biblioteca.Services.LibroService;

    import lombok.AllArgsConstructor;

    @Service
    @AllArgsConstructor
    public class LibroImplService implements LibroService {
        
        @Autowired
        private LibroRepository libroRepository;
        private LibroConversion libroConversion;

        @Override
        @Transactional
        public LibroDTO save(LibroDTO libroDTO) {

            Libro libro = libroConversion.convertirDTOALibro(libroDTO);
            libroRepository.save(libro);
            return libroConversion.convertirLibroADTO(libro);

        }

        @Override
        @Transactional
        public LibroDTO update(BigInteger id, LibroDTO libro){

            //busca el id del libro en la bd
            Optional<Libro> libroCurrentOptional = libroRepository.findById(id);

            //valida si el libro esta
            if(libroCurrentOptional.isPresent()){

                //convierto EL DTO que traia de parametro a entidad
                Libro libroCurrent = libroConversion.convertirDTOALibro(libro);
                libroCurrent.setId(id);
                libroCurrent.setTitulo(libro.getTitulo());
                libroCurrent.setAutor(libro.getAutor());
                libroCurrent.setGenero(libro.getGenero());
                libroCurrent.setAnioPublicacion(libro.getAnioPublicacion());
                libroCurrent.setInventario(libro.getInventario());

                //Guardado en la BD
                libroRepository.save(libroCurrent);

                return libroConversion.convertirLibroADTO(libroCurrent);
            }
            return null;     
        }

        @Override 
        public void deleteById(BigInteger id){
            libroRepository.deleteById(id);
        }

        @Override
        @Transactional(readOnly = true)
        public List<LibroDTO> findAll(){
            List<Libro> libros = (List<Libro>) libroRepository.findAll();
            return libros.stream()
                            .map(libro -> libroConversion.convertirLibroADTO(libro))
                            .toList();
        }

        @Override
        @Transactional(readOnly = true)
        public List<LibroDTO> findAvailableBooks(){
            List<Libro> libros = (List<Libro>) libroRepository.findAvailableBooks();
            return libros.stream()
                            .map(libro -> libroConversion.convertirLibroADTO(libro))
                            .toList();
        }

    }
    ```

    - **UsuarioImplService.java**
    ```
    package com.library.biblioteca.Services.Impl;

    import java.math.BigInteger;
    import java.util.List;
    import java.util.Optional;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import com.library.biblioteca.Configuration.UsuarioConversion;
    import com.library.biblioteca.Repository.UsuarioRepository;
    import com.library.biblioteca.Repository.Entities.Usuario;
    import com.library.biblioteca.Repository.EntitiesDTO.UsuarioDTO;
    import com.library.biblioteca.Services.UsuarioService;

    import lombok.AllArgsConstructor;

    @Service
    @AllArgsConstructor
    public class UsuarioImplService implements UsuarioService {
        
        @Autowired
        private UsuarioRepository usuarioRepository;
        private UsuarioConversion usuarioConversion;

        @Override
        @Transactional
        public UsuarioDTO save(UsuarioDTO usuarioDTO) {

            Usuario usuario = usuarioConversion.convertirDTOAUsuario(usuarioDTO);
            usuarioRepository.save(usuario);
            return usuarioConversion.convertirUsuarioADTO(usuario);

        }

        @Override
        @Transactional
        public UsuarioDTO update(BigInteger id, UsuarioDTO usuario){

            //busca el usuario por id en la bd
            Optional<Usuario> usuarioCurrentOptional = usuarioRepository.findById(id);

            //valida si el usuario esta
            if(usuarioCurrentOptional.isPresent()){

                //convierto EL DTO que traia de parametro a entidad
                Usuario usuarioCurrent = usuarioConversion.convertirDTOAUsuario(usuario);
                usuarioCurrent.setId(id);
                usuarioCurrent.setCedula(usuario.getCedula());

                //Guardado en la BD
                usuarioRepository.save(usuarioCurrent);

                return usuarioConversion.convertirUsuarioADTO(usuarioCurrent);
            }
            return null;     
        }

        @Override 
        public void deleteById(BigInteger id){
            usuarioRepository.deleteById(id);
        }

        @Override
        @Transactional(readOnly = true)
        public List<UsuarioDTO> findAll(){
            List<Usuario> usuarios = (List<Usuario>) usuarioRepository.findAll();
            return usuarios.stream()
                            .map(usuario -> usuarioConversion.convertirUsuarioADTO(usuario))
                            .toList();
        }

    }
    ```

    - **PrestamoImplService.java**
    ```
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
    ```

11. **Creamos los Controladores**
    - **LibroController.java**
    ```
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
    ```
    - **UsuarioController.java**
    ```
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
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.PutMapping;
    import org.springframework.web.bind.annotation.RequestBody;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;

    import com.library.biblioteca.Repository.EntitiesDTO.UsuarioDTO;
    import com.library.biblioteca.Services.UsuarioService;

    import lombok.AllArgsConstructor;

    @AllArgsConstructor
    @RestController
    @RequestMapping("/usuarios")
    public class UsuarioController {
        
        @Autowired
        private UsuarioService usuarioService;

        @GetMapping("/")
        public List<UsuarioDTO> findAll() {
            return usuarioService.findAll();
        }

        @PostMapping("/")
        public ResponseEntity<Map<String, Object>> save(@Validated @RequestBody UsuarioDTO usuarioDTO, BindingResult result) {
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

                UsuarioDTO usuarioSave = null;
                usuarioSave = usuarioService.save(usuarioDTO);

                response.put("mensaje", "El usuario ha sido creado con éxito");
                response.put("usuario", usuarioSave);

                return new ResponseEntity<>(response, HttpStatus.OK);

            } catch (DataAccessException e) {
                response.put("mensaje", "Error al realizar el guardado en la base de datos");
                response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

            }
        }

        @PutMapping("/{id}")
        public ResponseEntity<Map<String, Object>> update(@PathVariable BigInteger id, @Validated @RequestBody UsuarioDTO usuarioDTO, BindingResult result) {

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

                UsuarioDTO usuarioUpdate = null;
                usuarioUpdate = usuarioService.update(id, usuarioDTO);

                response.put("mensaje", "El usuario ha sido actualizado con éxito");
                response.put("usuario", usuarioUpdate);

                return new ResponseEntity<>(response, HttpStatus.OK);

            } catch (DataAccessException e) {
                response.put("mensaje", "Error al realizar el update en la base de datos");
                response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

            }
        }

        @DeleteMapping("/{id}")
        public void deleteById(@PathVariable BigInteger id){
            usuarioService.deleteById(id);
        } 
    }
    ```
    - **PrestamoController.java**
    ```
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
    ```
12. **Agregamos las siguientes dependendicas a pow.xml**
    ```
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>

    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>

    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.11.5</version>
    </dependency>
    ```
    - **pow.xml**
    ```
    <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
        <modelVersion>4.0.0</modelVersion>
        <parent>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-parent</artifactId>
            <version>3.2.4</version>
            <relativePath/> <!-- lookup parent from repository -->
        </parent>
        <groupId>com.library</groupId>
        <artifactId>biblioteca</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <name>biblioteca</name>
        <description>API RESTful in SPRING BOOT</description>
        <properties>
            <java.version>17</java.version>
        </properties>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-data-jpa</artifactId>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-security</artifactId>
            </dependency>
            <dependency>
                <groupId>io.jsonwebtoken</groupId>
                <artifactId>jjwt-jackson</artifactId>
                <version>0.11.5</version>
                <scope>runtime</scope>
            </dependency>
            <dependency>
                <groupId>io.jsonwebtoken</groupId>
                <artifactId>jjwt-impl</artifactId>
                <version>0.11.5</version>
                <scope>runtime</scope>
            </dependency>
            <dependency>
                <groupId>io.jsonwebtoken</groupId>
                <artifactId>jjwt-api</artifactId>
                <version>0.11.5</version>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-validation</artifactId>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
            </dependency>

            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-devtools</artifactId>
                <scope>runtime</scope>
                <optional>true</optional>
            </dependency>
            <dependency>
                <groupId>com.h2database</groupId>
                <artifactId>h2</artifactId>
                <scope>runtime</scope>
            </dependency>
            <dependency>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <optional>true</optional>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-test</artifactId>
                <scope>test</scope>
            </dependency>
            <dependency>
                <groupId>org.modelmapper</groupId>
                <artifactId>modelmapper</artifactId>
                <version>3.1.1</version>
            </dependency>
        </dependencies>

        <build>
            <plugins>
                <plugin>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-maven-plugin</artifactId>
                    <configuration>
                        <excludes>
                            <exclude>
                                <groupId>org.projectlombok</groupId>
                                <artifactId>lombok</artifactId>
                            </exclude>
                        </excludes>
                    </configuration>
                </plugin>
            </plugins>
        </build>

    </project>
    ```
13. **Creamos nuevas carpetas, nuestra estructura cambia**
    ``` 
    -Biblioteca
    -Configuration
    -Controllers
    -Exceptions
    -Repository
        -Entities
        -EntitiesDTO
        -Models
    -Security
    -Services
        -Impl
    -Utils
    ```

14. **Creamos la entidad Role y editamos la entidad Usuario**
    - **Role.java**
    ```
    package com.library.biblioteca.Repository.Entities;

    import java.math.BigInteger;

    import jakarta.persistence.Column;
    import jakarta.persistence.Entity;
    import jakarta.persistence.GeneratedValue;
    import jakarta.persistence.Id;
    import jakarta.persistence.Table;
    import jakarta.persistence.GenerationType;
    import lombok.Data;

    @Entity
    @Table(name = "roles")
    @Data
    public class Role {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private BigInteger id;
        @Column(name = "role_name")
        private String name;
        private String description;
    }
    ```

    - **Usuario.java**
    ```
    package com.library.biblioteca.Repository.Entities;

    import java.math.BigInteger;
    import java.util.Date;
    import java.util.List;

    import com.fasterxml.jackson.annotation.JsonFormat;

    import jakarta.persistence.Column;
    import jakarta.persistence.Entity;
    import jakarta.persistence.FetchType;
    import jakarta.persistence.GeneratedValue;
    import jakarta.persistence.GenerationType;
    import jakarta.persistence.Id;
    import jakarta.persistence.OneToMany;
    import jakarta.persistence.Table;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    @Entity
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Table(name = "usuario")
    public class Usuario {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private BigInteger id;

        @Column(nullable = false,unique = true)
        private Long cedula;

        private String email;

        @Column(name = "pwd")
        private String password;

        @OneToMany(fetch = FetchType.EAGER)
        private List<Role> roles;

        @Column(name="create_at")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date createAt; 
    }
    ```

15. **Creamos los modelos**
    - **JWTRequest.java**
    ```
    package com.library.biblioteca.Repository.Models;

    import lombok.Data;

    @Data
    public class JWTRequest {
        
        private String username;
        private String password;

    }
    ```
    - **JWTResponse.java**
    ```
    package com.library.biblioteca.Repository.Models;

    import lombok.AllArgsConstructor;
    import lombok.Data;

    @Data
    @AllArgsConstructor
    public class JWTResponse {
        
        private String jwt;

    }
    ```

16. **Creamos el Servicio JWTDetailService.java**
    - **JWTUserDetailService.java**
    ```
    package com.library.biblioteca.Services;

    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.User;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.stereotype.Service;

    import com.library.biblioteca.Repository.UsuarioRepository;

    import lombok.AllArgsConstructor;


    @Service
    @AllArgsConstructor
    public class JWTUserDetailService implements UserDetailsService {

        private final UsuarioRepository usuarioRepository;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            return this.usuarioRepository.findByEmail(username)
                    .map(usuario -> {
                        final var authorities = usuario.getRoles()
                                .stream()
                                .map(role -> new SimpleGrantedAuthority(role.getName()))
                                .toList();
                        return new User(usuario.getEmail(), usuario.getPassword(), authorities);
                    }).orElseThrow(() -> new UsernameNotFoundException("User not exist"));                
            }

        
    }
    ```

17. **Creamos el Servicio JWTService**
    - **JWTService.java**
    ```
    package com.library.biblioteca.Services;

    import java.nio.charset.StandardCharsets;
    import java.util.Collections;
    import java.util.Date;
    import java.util.Map;
    import java.util.function.Function;

    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.stereotype.Service;

    import io.jsonwebtoken.Claims;
    import io.jsonwebtoken.Jwts;
    import io.jsonwebtoken.security.Keys;

    @Service
    public class JWTService {

        public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
        public static final String JWT_SECRET= "jxgEQe.XHuPq8VdbyYFNkAN.dudQ0903YUn4";

        private Claims getAllClaimsFromToken(String token) {
            final var key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
            return Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }

        public <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver) {
            final var claims = this.getAllClaimsFromToken(token);
            return claimsResolver.apply(claims);
        }

        private Date getExpirationDateFromToken(String token) {
            return this.getClaimsFromToken(token, Claims::getExpiration);
        }

        private Boolean isTokenExpired(String token) {
            final var expirationDate = this.getExpirationDateFromToken(token);
            return expirationDate.before(new Date());
        }

        public String getUsernameFromToken(String token) {
            return this.getClaimsFromToken(token, Claims::getSubject);
        }

        public  String generateToken(UserDetails userDetails) {
            final Map<String, Object> claims = Collections.singletonMap("ROLES", userDetails.getAuthorities().toString());
            return this.getToken(claims, userDetails.getUsername());
        }
        private String getToken(Map<String, Object> claims, String subject) {
            final var key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));

            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(subject)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                    .signWith(key)
                    .compact();
        }

        public  Boolean validateToken(String token, UserDetails userDetails) {
            final var usernameFromUserDetails  = userDetails.getUsername();
            final var usernameFromJWT  = this.getUsernameFromToken(token);

            return (usernameFromUserDetails.equals(usernameFromJWT)) && !this.isTokenExpired(token);
        }
    }
    ```

18. **Creamos el componente JwtAuthenticationEntryPoint**
    - **JwtAuthenticationEntryPoint.java**
    ```
    package com.library.biblioteca.Components;

    import java.io.IOException;

    import org.springframework.security.core.AuthenticationException;
    import org.springframework.security.web.AuthenticationEntryPoint;
    import org.springframework.stereotype.Component;


    import jakarta.servlet.ServletException;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;

    @Component
    public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

        @Override
        public void commence(HttpServletRequest request,
                            HttpServletResponse response,
                            AuthenticationException authException) throws IOException, ServletException {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
    }
    ```

19. **Creamos la Seguridad en la carpeta Security**
    - **CsrfCookieFilter.java**
    ```
    package com.security.jwt.security;

    import jakarta.servlet.FilterChain;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import org.springframework.security.web.csrf.CsrfToken;
    import org.springframework.web.filter.OncePerRequestFilter;

    import java.io.IOException;
    import java.util.Objects;

    public class CsrfCookieFilter extends OncePerRequestFilter {

        @Override
        protected void doFilterInternal(HttpServletRequest request,
                HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

            var csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

            if (Objects.nonNull(csrfToken.getHeaderName())) {
                response.setHeader(csrfToken.getHeaderName(), csrfToken.getToken());
            }

            filterChain.doFilter(request, response);

        }
    }
    ```

    - **JWTValidationFilter.java**
    ```
    package com.library.biblioteca.Security;

    import io.jsonwebtoken.ExpiredJwtException;
    import jakarta.servlet.FilterChain;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import lombok.AllArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
    import org.springframework.stereotype.Component;
    import org.springframework.web.filter.OncePerRequestFilter;

    import com.library.biblioteca.Services.JWTService;
    import com.library.biblioteca.Services.JWTUserDetailService;

    import java.io.IOException;
    import java.util.Objects;

    @Component
    @AllArgsConstructor
    @Slf4j
    public class JWTValidationFilter extends OncePerRequestFilter {

        private final JWTService jwtService;
        private final JWTUserDetailService jwtUserDetailService;


        public static final String AUTHORIZATION_HEADER = "Authorization";
        public static final String AUTHORIZATION_HEADER_BEARER = "Bearer ";

        @Override
        protected void doFilterInternal(HttpServletRequest request,
                                                        HttpServletResponse response,
                                                        FilterChain filterChain) throws ServletException, IOException {
            final var requestTokenHeader = request.getHeader(AUTHORIZATION_HEADER);
            String username = null;
            String jwt = null;

            if(Objects.nonNull(requestTokenHeader)
                    && requestTokenHeader.startsWith(AUTHORIZATION_HEADER_BEARER)) {
                jwt = requestTokenHeader.substring(7);

                try {
                    username = jwtService.getUsernameFromToken(jwt);
                } catch (IllegalArgumentException e) {
                    log.error(e.getMessage());
                } catch (ExpiredJwtException e) {
                    log.warn(e.getMessage());
                }
            }

            if (Objects.nonNull(username) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
                final var userDetails = this.jwtUserDetailService.loadUserByUsername(username);

                if (this.jwtService.validateToken(jwt, userDetails)) {
                    var usernameAndPassAuthToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    usernameAndPassAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernameAndPassAuthToken);
                }
            }
            filterChain.doFilter(request, response);
        }
    }
    ```

    - **SecurityConfig.java**
    ```
    package com.library.biblioteca.Security;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.http.HttpMethod;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.config.Customizer;
    import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.crypto.password.NoOpPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
    import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
    import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
    import org.springframework.web.cors.CorsConfiguration;
    import org.springframework.web.cors.CorsConfigurationSource;
    import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

    import java.util.List;

    @Configuration
    public class SecurityConfig {

        @Bean
        @Autowired
        SecurityFilterChain securityFilterChain(HttpSecurity http, JWTValidationFilter jwtValidationFilter)
                throws Exception {
            http.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
            var requestHandler = new CsrfTokenRequestAttributeHandler();
            requestHandler.setCsrfRequestAttributeName("_csrf");
            http.authorizeHttpRequests(auth -> auth
                    .requestMatchers("/h2-console", "/authenticate").permitAll()
                    .requestMatchers(HttpMethod.POST,"/prestamos/").hasAnyRole("USER","ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/prestamos/devolver/{id}").hasAnyRole("USER", "ADMIN")
                    .requestMatchers("/libros/disponibles").hasRole("USER")
                    .requestMatchers( "/libros/**","/prestamos/**", "/prestamos/**", "/usuarios/**").hasRole("ADMIN")
                    .requestMatchers("/libros/**", "/prestamos/**").hasRole("BIBLIOTECARIO")
                    .anyRequest().denyAll())
                    .formLogin(Customizer.withDefaults())
                    .httpBasic(Customizer.withDefaults());
            http.addFilterAfter(jwtValidationFilter, BasicAuthenticationFilter.class);
            http.cors(cors -> corsConfigurationSource());
            http.csrf(csrf -> csrf
                    .csrfTokenRequestHandler(requestHandler)
                    .ignoringRequestMatchers("/authenticate","/h2-console","/prestamos/**","/libros/**","/usuarios/**")
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                    .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class);
            return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return NoOpPasswordEncoder.getInstance();
        }

        @Bean
        CorsConfigurationSource corsConfigurationSource() {
            var config = new CorsConfiguration();

            config.setAllowedOrigins(List.of("*"));
            config.setAllowedMethods(List.of("*"));
            config.setAllowedHeaders(List.of("*"));

            var source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", config);

            return source;
        }

        @Bean
        AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
            return configuration.getAuthenticationManager();
        }

    }
    ```

20. **Creamos el AuthenticationController**
    - **AuthenticationController.java**
    ```
    package com.library.biblioteca.Controllers;

    import lombok.AllArgsConstructor;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.BadCredentialsException;
    import org.springframework.security.authentication.DisabledException;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestBody;
    import org.springframework.web.bind.annotation.RestController;

    import com.library.biblioteca.Repository.Models.JWTRequest;
    import com.library.biblioteca.Repository.Models.JWTResponse;
    import com.library.biblioteca.Services.JWTService;
    import com.library.biblioteca.Services.JWTUserDetailService;


    @RestController
    @AllArgsConstructor
    public class AuthenticationController {

        private final AuthenticationManager authenticationManager;
        private final JWTUserDetailService jwtUserDetailService;
        private final JWTService jwtService;

        @PostMapping("/authenticate")
        public ResponseEntity<?> postToken(@RequestBody JWTRequest request) {
            this.authenticate(request);

            final var userDetails = this.jwtUserDetailService.loadUserByUsername(request.getUsername());

            final var token = this.jwtService.generateToken(userDetails);
            return ResponseEntity.ok(new JWTResponse(token));
        }

        private void authenticate(JWTRequest request) {
            try {
                this.authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            } catch (BadCredentialsException | DisabledException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }
    ```
