package com.borrador.appservicios.controladores;

import com.borrador.appservicios.Exception.MiException;
import com.borrador.appservicios.entidades.Contrato;
import com.borrador.appservicios.entidades.Usuario;
import com.borrador.appservicios.enumeradores.Categoria;
import com.borrador.appservicios.enumeradores.Rol;

import com.borrador.appservicios.excepciones.Excepciones;
import com.borrador.appservicios.servicios.ComentarioServicio;
import com.borrador.appservicios.servicios.ContratoServicio;
import com.borrador.appservicios.servicios.ServicioServicio;
import com.borrador.appservicios.servicios.UsuarioServicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author kyouma
 */
@Controller
@RequestMapping("/")
public class IndexControlador {

    Usuario usuario;
    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ContratoServicio contratoServicio;

    // --- INDEX --- //
    @GetMapping("/")
    public String Index() {
        return "index.html";
    }

    // --- Registrar Usuario --- //
    @GetMapping("/registrar-usuario")
    public String registrarUsuario(HttpSession session, ModelMap modelo) {
        modelo.addAttribute("categorias", Categoria.values());
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        if (logueado != null) {
            return "redirect:/";
        } else {
            return "usuario_registro.html";
        }
    }

    @PostMapping("/registro")
    public String registrarUsuario(@RequestParam String email, @RequestParam String password, @RequestParam String password2,
            @RequestParam String nombre, @RequestParam String apellido,
            ModelMap modelo, @RequestParam(required = false) MultipartFile archivo) {

        try {
            usuarioServicio.persistirUsuario(email, password, password2, nombre, apellido, archivo);
            modelo.put("exito", "usuario registrado correctamente");
            return "redirect:/";

        } catch (Excepciones ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            return "redirect:/";
        }
    }

    // --- Login --- //
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo, String password, HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        if (error != null) {
            session.invalidate();
            modelo.put("error", "Lo sentimos, el usuario o la contraseña no coinciden.");

            System.out.println("");
            return "login.html"; // Retornar inmediatamente en caso de error

        }
        if (logueado != null) {

            System.out.println("");
            return "redirect:/";
        } else {
            return "login.html";
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_ADMIN', 'ROLE_MODERADOR')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session, ModelMap modelo) {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        if (logueado.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }

        return "redirect:/";
    }

    // --- Perfil Usuario --- //
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_ADMIN', 'ROLE_MODERADOR')")
    @GetMapping("/perfil/{id}")
    public String perfilUsuario(@PathVariable String id, HttpSession session, ModelMap modelo) {

        session.setAttribute("usuariosession", usuarioServicio.getOne(id));
        usuario = (Usuario) session.getAttribute("usuariosession");

        if (usuario != null && usuario.getId().equals(id)) {
            System.out.println("-    ENTANDO A PERFIL DE USUARIO    ----------- ROL -----" + usuario.getRol());
            return "perfiles.html";
        } else {
            System.out.println("-    USUARIO NULO   ----------------");
            return "redirect:/";
        }
    }

    // --- Modificar Usuario --- //
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_ADMIN', 'ROLE_MODERADOR')")
    @GetMapping("/perfils/{id}")
    public String perfil(ModelMap modelo, HttpSession session) {
        usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.put("usuario", usuario);

        return "modificar_cliente.html";
    }

    // --- Actualizar Datos Usuario POST FORM ACTUALIZAR USUARIO --- //
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_ADMIN', 'ROLE_MODERADOR')")
    @PostMapping("/perfils/{id}")
    public String actualizar(MultipartFile archivo, @PathVariable String id, @RequestParam String email, @RequestParam String nombre,
            @RequestParam String apellido, @RequestParam String telefono,
            @RequestParam String password, @RequestParam String password2, ModelMap modelo, HttpSession session) {

        try {
            Usuario usuarioactualizado = usuarioServicio.actualizar(id, email, nombre, apellido, telefono, archivo, password, password2);

            modelo.put("exito", "Usuario actualizado correctamente!");

            Usuario logueado = (Usuario) session.getAttribute("usuariosession");

            if (logueado.getRol().toString().equals("ADMIN")) {
                session.setAttribute("usuariosession", usuarioactualizado);
                return "redirect:/admin/dashboard";
            } else {
                session.setAttribute("usuariosession", usuarioactualizado);
                return "redirect:/perfil/" + id;
            }
        } catch (Exception ex) {

            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            modelo.put("password", password);

            return "modificar_cliente.html";
        }

    }

    // --- Eliminar Imagen --- //
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_ADMIN', 'ROLE_MODERADOR')")
    @GetMapping("/perfils/emilinar-foto/{id}")
    public String eliminarFoto(@PathVariable String id, HttpSession session, MultipartFile archivo, RedirectAttributes redirectAttributes) {
        try {
            Usuario usuarioActual = (Usuario) session.getAttribute("usuariosession");
            System.out.println(usuarioActual.getId());
            System.out.println(id);
            if (usuarioActual != null && usuarioActual.getId().equals(id)) {
                Usuario usuarioActualizado = usuarioServicio.eliminarImagenDeUsuario(id);
                session.setAttribute("usuariosession", usuarioActualizado);
                return "redirect:/perfil/" + id;

            } else {
                System.out.println("estamos en else");
                throw new Exception("No tienes permisos");

            }
        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("msj", e.getMessage());
            System.out.println(" estamos en catch: " + e.getMessage());
            return "redirect:/error";
        }
    }

    // --- Alta Usuario --- //
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_ADMIN', 'ROLE_MODERADOR')")
    @GetMapping("/perfils/modificar-alta/{id}")
    public String cambiarAltaUser(@PathVariable String id, HttpSession session) {

        System.out.println("CAMBIANDO ALTA-------");

        Usuario usuarioactualizado = usuarioServicio.cambiarAlta(id);

        session.setAttribute("usuariosession", usuarioactualizado);

        System.out.println("");
        System.out.println(usuarioactualizado.getActivo());
        System.out.println("controlador");
        return "redirect:/perfil/" + id;
    }

    /*
       -------------------------------------------------------
       -------------------------------------------------------
                             PROVEEDOR
       -------------------------------------------------------
       -------------------------------------------------------
     */
    @Autowired
    private ComentarioServicio comentarioServicio;

    @Autowired
    private ServicioServicio servicioServicio;

    @PostMapping("/registro2")
    public String registrarProveedor(@RequestParam String nombre, @RequestParam String apellido, @RequestParam String telefono,
            @RequestParam Categoria categoria, @RequestParam String email, @RequestParam String password,
            @RequestParam String password2, ModelMap modelo, @RequestParam(required = false) MultipartFile archivo) throws MiException {
        try {
            usuarioServicio.crearProveedor(nombre, apellido, telefono, email, password, password2, categoria, archivo);
            modelo.put("exito", "proveedor registrado correctamente");
            return "redirect:/";

        } catch (Excepciones ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            return "redirect:/";
        }
    }

    // --- Servicios Vista --- //
    @GetMapping("/servicios")
    public String servicios(ModelMap modelo) {
        List<Usuario> proveedores = usuarioServicio.listarProveedores();
        List<Usuario> electricidad = usuarioServicio.listarElectricidad();
        List<Usuario> plomeria = usuarioServicio.listarPlomeria();
        List<Usuario> salud = usuarioServicio.listarSalud();
        List<Usuario> limpieza = usuarioServicio.listarLimpieza();
        List<Usuario> jardineria = usuarioServicio.listarJardineria();
        List<Usuario> varios = usuarioServicio.listarVarios();

        modelo.addAttribute("categorias", Categoria.values());
        modelo.addAttribute("plomeria", plomeria);
        modelo.addAttribute("electricidad", electricidad);
        modelo.addAttribute("salud", salud);
        modelo.addAttribute("limpieza", limpieza);
        modelo.addAttribute("jardineria", jardineria);
        modelo.addAttribute("varios", varios);

        return "servicios_pruebas.html";
    }

    // --- Proveedor Perfil Vista --- //
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_ADMIN', 'ROLE_MODERADOR')")
    @GetMapping("/servicios/proveedor/{id}")
    public String servicios(ModelMap modelo, @PathVariable String id) {

        modelo.put("proveedor", usuarioServicio.getOne(id));

        return "servicio_pruebas.html";
    }

    //---------- Comentarios ------------//
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_ADMIN', 'ROLE_MODERADOR')")
    @PostMapping("/servicios/proveedor/{id}")
    public String comentar(@RequestParam String comentario,
            @RequestParam String id,// id de usuario
            @RequestParam String idProveedor,
            ModelMap modelo) {

        try {

            comentarioServicio.persistirComentario(comentario, id, idProveedor);

            modelo.put("exito", "COMENTARIO REALIZADO CORRECTAMENTE");
            return "redirect:/servicios/proveedor/" + idProveedor;
        } catch (Excepciones ex) {
            System.out.println("1----------- EXCEPCION DE CARGA DE COMENTARIO");
            Logger.getLogger(IndexControlador.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "servicio_pruebas.html";
    }

    // --- Comentarios en Contrato --- //
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_ADMIN', 'ROLE_MODERADOR')")
    @PostMapping("/servicios/comentario/{id}")
    public String comentarServicio(@RequestParam String comentario,
            @RequestParam String id,// id de usuario
            @RequestParam String idProveedor,
            @RequestParam String idContrato,
            ModelMap modelo) {

        try {

            comentarioServicio.persistirComentarioServicio(comentario, id, idProveedor, idContrato);

            modelo.put("exito", "COMENTARIO REALIZADO CORRECTAMENTE");
            return "redirect:/servicios-contratados/" + id;
        } catch (Excepciones ex) {
            System.out.println("1----------- EXCEPCION DE CARGA DE COMENTARIO");
            Logger.getLogger(IndexControlador.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "servicio_pruebas.html";
    }

    // --- Modificar Comentarios en Proveedor --- //
    @PostMapping("/modificar-comentario/{id}")
    public String modificarComentarioProveedor(@RequestParam String idProv, @RequestParam String idComent, @RequestParam String comentario) {

        try {
            comentarioServicio.modificarComentarioProveedor(idComent, comentario);
            System.out.println("---");
            System.out.println("--- Comentario modificado ---");
            System.out.println("---");
            return "redirect:/servicios/proveedor/"+idProv;

        } catch (Exception e) {
            return "index.html";
        }

    }
    
    // --- eliminar comentario en Proveedor --- //
    @PostMapping("/eliminar-comentario-proveedor/{id}")
    public String eliminarComentarioProveedor(@RequestParam String idProv, @RequestParam String idComent) throws Excepciones{
        try {
            if(!idComent.isEmpty() && !idProv.isEmpty()){
                
                System.out.println("idComentario - "+idComent);
                System.out.println("idContrato - "+idProv);
                
                comentarioServicio.eliminarComentarioProveedor(idComent, idProv);
                System.out.println("---");
                System.out.println("--- Comentario eliminado ---");
                System.out.println("---");
                return "redirect:/servicios/proveedor/"+idProv;
            }
        } catch (Exception e) {
            throw new Excepciones("ERROR AL ELIMINAR COMENTARIO --- CONTROLADOR ---");
        }
        
        return "index.html";
    }
    
    // --- Modificar Comentarios en contrato --- //
    @PostMapping("/modificar-comentario-usuario/{id}")
    public String modificarComentarioContrato(@PathVariable String id,@RequestParam String idUser, @RequestParam String comentario) {

        try {
            comentarioServicio.modificarComentarioContrato(id, comentario);
            System.out.println("---");
            System.out.println("--- Comentario modificado ---");
            System.out.println("---");
            return "redirect:/servicios-contratados/"+idUser;

        } catch (Exception e) {
            return "servicios_contratados_lista.html";
        }

    }

    // --- eliminar comentario en contrato --- //
    @PostMapping("/eliminar-comentario/{id}")
    public String eliminarComentarioContrato(@RequestParam String idUser ,@RequestParam String idContrato, @RequestParam String idComent) throws Excepciones{
        try {
            if(!idComent.isEmpty() && !idContrato.isEmpty()){
                
                System.out.println("idComentario - "+idComent);
                System.out.println("idContrato - "+idContrato);
                
                comentarioServicio.eliminarComentarioContrato(idComent, idContrato);
                System.out.println("---");
                System.out.println("--- Comentario eliminado ---");
                System.out.println("---");
                return "redirect:/servicios-contratados/"+idUser;

            }
        } catch (Exception e) {
            throw new Excepciones("ERROR AL ELIMINAR COMENTARIO --- CONTROLADOR ---");
        }
        
        return "index.html";
    }
 
    
    // --- Proveedor Registrar un Servicio --- //
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_ADMIN', 'ROLE_MODERADOR')")
    @GetMapping("registro-servicio/{id}")
    public String registroServicio(ModelMap modelo) {

        modelo.addAttribute("categorias", Categoria.values());

        return "registrar_servicio_form.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_ADMIN', 'ROLE_MODERADOR')")
    @PostMapping("/registrar-servicio/{id}")
    public String crearServicio(@PathVariable String id, @RequestParam String descripcionServicio,
            @RequestParam Integer precioServicio) {

        try {
            servicioServicio.crearServicio(id, descripcionServicio, precioServicio);
            System.out.println("CONTROLADOR PROVEEDOR--- SERVICIO CARGADO---POST");
            return "redirect:/perfil/" + id;
        } catch (Excepciones e) {
            System.out.println("CATCH ERROR EN POST SERVICIO CREAR SERVICIO");
            return "index.html";
        }

    }

    // --- Servicios Contratados Vista --- //
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_ADMIN', 'ROLE_MODERADOR')")
    @GetMapping("/servicios-contratados/{id}")
    public String serviciosContratados(@PathVariable String id, HttpSession session, ModelMap modelo) {
        usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("usuario", usuario);

        if (usuario.getRol() == Rol.CLIENTE) {
            // TRAE CONTRATOS CLIENTE
            List<Contrato> contratosCliente = contratoServicio.listarContratosPorCliente(usuario);
            modelo.addAttribute("contratos", contratosCliente);
        } 
        if (usuario.getRol() == Rol.PROVEEDOR) {
            // TRAE CONTRATOS PROVEEDOR
            List<Contrato> contratosProveedor = contratoServicio.listarContratosPorProveedor(usuario);
            modelo.addAttribute("contratos", contratosProveedor);
        }
        if (usuario.getRol() == Rol.MODERADOR) {
            // TRAE CONTRATOS MODERADOR
            List<Contrato> contratosModerador = contratoServicio.listarContratosPorCliente(usuario);
            modelo.addAttribute("contratos", contratosModerador);
        }
        if (usuario.getRol() == Rol.ADMIN) {
            // TRAE CONTRATOS MODERADOR
            List<Contrato> contratosAdministrador = contratoServicio.listarContratosPorCliente(usuario);
            modelo.addAttribute("contratos", contratosAdministrador);
        }

        return "servicios_contratados_lista.html";
    }

}
