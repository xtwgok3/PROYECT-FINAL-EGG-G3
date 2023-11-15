package com.borrador.appservicios.servicios;

import com.borrador.appservicios.entidades.Imagen;
import com.borrador.appservicios.excepciones.Excepciones;
import com.borrador.appservicios.repositorios.ImagenRepositorio;
import java.io.IOException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author facun
 */
@Service
public class ImagenServicio {

    @Autowired
    private ImagenRepositorio imagenRepositorio;

    @Transactional
    public Imagen guardar(MultipartFile archivo) throws Excepciones {

        if (archivo != null) {
            try {

                Imagen imagen = new Imagen();

                imagen.setMime(archivo.getContentType());

                imagen.setNombre(archivo.getOriginalFilename());// antes ---.getName()

                imagen.setContenido(archivo.getBytes());

                return imagenRepositorio.save(imagen);

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    @Transactional
    public Imagen actualizar(MultipartFile archivo, String idImagen) {

        if (archivo != null) {
            try {

                Imagen imagen = new Imagen();

                if (idImagen != null) {
                    Optional<Imagen> respuesta = imagenRepositorio.findById(idImagen);

                    if (respuesta.isPresent()) {
                        imagen = respuesta.get();
                    }

                }

                imagen.setMime(archivo.getContentType());

                imagen.setNombre(archivo.getOriginalFilename());//.getName()

                imagen.setContenido(archivo.getBytes());

                return imagenRepositorio.save(imagen);

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    /*public Imagen monito(){
        return (Imagen) imagenRepositorio.findByNombre("monito.png");
    }
     */
}
