package com.borrador.appservicios.repositorios;

import com.borrador.appservicios.entidades.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author facun
 */
@Repository
public interface ImagenRepositorio extends JpaRepository<Imagen, String>{

    public Object findByNombre(String string);
    
}
