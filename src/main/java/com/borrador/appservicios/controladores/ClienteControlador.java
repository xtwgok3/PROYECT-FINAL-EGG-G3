/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borrador.appservicios.controladores;

import com.borrador.appservicios.entidades.Usuario;
import com.borrador.appservicios.excepciones.Excepciones;
import com.borrador.appservicios.servicios.ContratoServicio;
import com.borrador.appservicios.servicios.ServicioServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author facun
 */
@Controller
public class ClienteControlador {

    @Autowired
    private ContratoServicio contratoServicio;

    @Autowired
    private ServicioServicio servicioServicio;

    // --- Vista de CONTRATO SERVICIO --- //
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_ADMIN', 'ROLE_MODERADOR')")
    @GetMapping("/cita/{id}")
    public String citaProgramada(@PathVariable String id, ModelMap modelo) {
        modelo.addAttribute("servicio", servicioServicio.buscarServicioPorId(id));
        return "contrato.html";
    }

    // --- CONFIRMACION DE CONTRATO - FORMULARIO POST --- //
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_ADMIN', 'ROLE_MODERADOR')")
    @PostMapping("/cita/{id}")
    public String contratoCita(@RequestParam String idCliente, @RequestParam String idProveedor, @RequestParam String idServicio,
            @RequestParam Integer precio, @RequestParam String nombre, @RequestParam String apellido,
            @RequestParam String telefono, @RequestParam String direccion, HttpSession session,
            ModelMap modelo) throws Excepciones {

        try {

            Usuario usuario = (Usuario) session.getAttribute("usuariosession");

            System.out.println(" --- ");
            System.out.println("CONTRATO POR CONFIRMAR");
            System.out.println(" --- ");
            System.out.println(" --- CONTROLADOR CLIENTE... id Servicio : " + idServicio);

            contratoServicio.crearContrato(idProveedor, idCliente, idServicio, precio, nombre, apellido, telefono, direccion);
            System.out.println(" --- ");
            System.out.println(" CONTRATO CONFIRMADO --- OK");
            System.out.println(" --- ");

            session.setAttribute("usuariosession", usuario);

            return "redirect:/servicios";
        } catch (Exception e) {
            throw new Excepciones("ERROR CREAR CLIENTE CONTROLADOR");
        }

    }

    // --- CANCELAR CONTRATO --- //
    @GetMapping("/cancelar-contrato/{id}")
    public String cancelarContrato(@PathVariable String id) throws Excepciones {
        try {
            System.out.println("Tratanco de cancelar contrato de id " + id);
            contratoServicio.contratoCancelar(id);
            return "perfiles.html";

        } catch (Exception e) {
            throw new Excepciones("FALLA AL TRATAR DE CANCELAR CONTRATO");
        }
    }

}
