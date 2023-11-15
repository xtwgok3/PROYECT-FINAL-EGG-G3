package com.borrador.appservicios.entidades;

import com.borrador.appservicios.enumeradores.Categoria;
import com.borrador.appservicios.enumeradores.Rol;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author facun
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Usuario {

    @Id
    @GeneratedValue(generator = "uuid")// Generar id alfanumerico unico
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;
    private String password;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date ultimaConexion;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    private Boolean activo;

    private Integer intentos;

    // @OneToOne(cascade = CascadeType.PERSIST)
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "imagen_id", nullable = true)
    private Imagen imagen;

    // atributos comunes de Cliente y Proveedor----
    private String nombre;
    private String apellido;
    private String direccion;
    private String telefono;
        
    // atributos propios de Proveedor
    private Categoria categoriaServicio;  // categoria general para iterar dentro del menu de proveedores 
    
    @OneToMany
    private List<Comentario> comentarios;

    @OneToMany
    private List<Servicio> servicios;

}
