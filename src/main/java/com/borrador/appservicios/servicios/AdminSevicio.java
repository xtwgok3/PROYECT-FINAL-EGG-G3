package com.borrador.appservicios.servicios;

import com.borrador.appservicios.entidades.Imagen;
import com.borrador.appservicios.entidades.Usuario;
import com.borrador.appservicios.enumeradores.Rol;
import com.borrador.appservicios.excepciones.Excepciones;
import com.borrador.appservicios.repositorios.ImagenRepositorio;
import com.borrador.appservicios.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Kyouma
 */
@Service
public class AdminSevicio implements UserDetailsService {

    Usuario usuario;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private ImagenRepositorio imagenRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    public Usuario getOne(String id) {
        return usuarioRepositorio.getOne(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        usuario = usuarioRepositorio.buscarPorEmail(email);

        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString()); //ROLE_USER
            permisos.add(p);
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);
            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        } else {
            return null;
        }

    }

    @Transactional(readOnly = true)
    public List<Usuario> listarUsuarios() {
        List<Usuario> usuarios = new ArrayList();
        usuarios = usuarioRepositorio.findAll();
        return usuarios;
    }

    @Transactional
    public Usuario Alta(String id) {
        Optional<Usuario> resp = usuarioRepositorio.findById(id);

        if (resp.isPresent()) {
            usuario = resp.get();
            boolean b = usuario.getActivo();
            usuario.setActivo(!b);

            System.out.println("");
            System.out.println(usuario.getActivo());
            System.out.println("Alta servicio");

            System.out.println("");
            System.out.println("");
            System.out.println("email: " + usuario.getEmail());

            System.out.println("");
            System.out.println("");

            return usuario;

        }

        return usuario;
    }

//    @Transactional
//    public void cambiarRol(String id) {
//
//        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
//
//        if (respuesta.isPresent()) {
//
//            usuario = respuesta.get();
//
//            if (usuario.getRol().equals(Rol.USER)) {
//                usuario.setRol(Rol.ADMIN);
//            } else if (usuario.getRol().equals(Rol.ADMIN)) {
//                usuario.setRol(Rol.USER);
//            }
//
//        }
//
//    }

    //---------- Cambio Roles ----------
    @Transactional
    public void cambiarRolUser(String id) {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            usuario = respuesta.get();
            usuario.setRol(Rol.USER);
            System.out.println("--");
            System.out.println("Rol Modificado " + usuario.getEmail() + " a -" + usuario.getRol());
            System.out.println("--");
        }
    }

    @Transactional
    public void cambiarRolCiente(String id) {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            usuario = respuesta.get();
            usuario.setRol(Rol.CLIENTE);
            System.out.println("--");
            System.out.println("Rol Modificado " + usuario.getEmail() + " a -" + usuario.getRol());
            System.out.println("--");

        }
    }

    @Transactional
    public void cambiarRolProveedor(String id) {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            usuario = respuesta.get();
            usuario.setRol(Rol.PROVEEDOR);
            System.out.println("--");
            System.out.println("Rol Modificado " + usuario.getEmail() + " a -" + usuario.getRol());
            System.out.println("--");

        }
    }

    @Transactional
    public void cambiarRolAdmin(String id) {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            usuario = respuesta.get();
            usuario.setRol(Rol.ADMIN);
            System.out.println("--");
            System.out.println("Rol Modificado " + usuario.getEmail() + " a -" + usuario.getRol());
            System.out.println("--");
        }
    }

    @Transactional
    public void cambiarRolMod(String id) {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            usuario = respuesta.get();
            usuario.setRol(Rol.MODERADOR);
            System.out.println("--");
            System.out.println("Rol Modificado " + usuario.getEmail() + " a -" + usuario.getRol());
            System.out.println("--");

        }
    }

    // ------ Eliminar Cuenta de la BD ------------//
    @Transactional
    public void eliminar(String idUsuario) {
        try {
            Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);
            System.out.println("Usuario : " + respuesta.get().getEmail().toString());
            System.out.println("Usuario id : " + respuesta.get().getId().toString());
            if (respuesta.isPresent()) {
                System.out.println("Usuario : " + respuesta.get().getEmail().toString());
                usuarioRepositorio.deleteById(respuesta.get().getId());

                System.out.println("");
                System.out.println("Usuario eliminado");
                System.out.println("");
            }
        } catch (Exception e) {
            System.out.println("ERROR AL ELIMIAR USUARIO");
        }

    }

    @Transactional
    public Usuario actualizar(MultipartFile archivo, String id, String nombre,
            String apellido, String email) throws Exception {

        //validar(nombre, apellido, email, password, password2);
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {
            usuario = respuesta.get();
            // Verificar que el usuario respuesta coincida con el id
            if (respuesta.get().getId().equals(id)) {
                usuario.setNombre(nombre);
                usuario.setApellido(apellido);
                usuario.setEmail(email);

                cargarImagen(archivo);

                usuarioRepositorio.save(usuario);
                System.out.println("-------------------------------------------------------------");
                System.out.println("Perfil Actualizado: " + usuario.getEmail());
                System.out.println("-------------------------------------------------------------");
                return usuario;
            } else {
                System.out.println("estas en el else ");
                throw new Exception("La contraseña proporcionada no coincide con la contraseña en la base de datos");
            }
        }

        return null;
    }
    //carga imagen nula si no sube un archivo

    public boolean cargarImagen(MultipartFile archivo) throws Excepciones {
        if (!archivo.isEmpty()) {
            Imagen imagen = imagenServicio.guardar(archivo);
            usuario.setImagen(imagen);
            return true;
        } else //usuario.setImagen(null);
        {
            return false;
        }

    }

}
