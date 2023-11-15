/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borrador.appservicios.servicios;

import com.borrador.appservicios.entidades.Servicio;
import com.borrador.appservicios.entidades.Usuario;
import com.borrador.appservicios.excepciones.Excepciones;
import com.borrador.appservicios.repositorios.ServicioRepositorio;
import com.borrador.appservicios.repositorios.UsuarioRepositorio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author facun
 */
@Service
public class ServicioServicio {

    @Autowired
    ServicioRepositorio servicioRepositorio;
    
    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    
    @Transactional
    public void crearServicio(String idProveedor, String descripcionServicio,
            Integer precioServicio) throws Excepciones {

        Optional<Usuario> resp = usuarioRepositorio.findById(idProveedor);
        if (resp.isPresent()) {

            Usuario proveedor = resp.get();
            Servicio servicio = new Servicio();

            servicio.setProveedor(proveedor);
            servicio.setDescripcionServicio(descripcionServicio);
            //  servicio.setCategoriaServicio(categoriaServicio);
            servicio.setPrecioServicio(precioServicio);

            //   servicio.setContrato(null);
            //   servicio.setCalificacion(null);
            System.out.println("CARGANDO SERVICIO A PROVEEDOR" + proveedor.getNombre());
            proveedor.getServicios().add(servicio);
            System.out.println("CARGANDO SERVICIO EN BASE DE DATOS..  " + servicio.getDescripcionServicio());

            servicioRepositorio.save(servicio);
        }
    }

    public Servicio buscarServicioPorId(String id){
        Optional<Servicio> resp = servicioRepositorio.findById(id);
        Servicio servicio = new Servicio();
        if(resp.isPresent()){
            servicio = resp.get();
        }
        return servicio;
    }

}
