package com.borrador.appservicios.servicios;

import com.borrador.appservicios.Exception.MiException;
import com.borrador.appservicios.entidades.Imagen;
import com.borrador.appservicios.entidades.Usuario;
import com.borrador.appservicios.enumeradores.Categoria;
import com.borrador.appservicios.enumeradores.Rol;
import com.borrador.appservicios.excepciones.Excepciones;
import com.borrador.appservicios.repositorios.ImagenRepositorio;
import com.borrador.appservicios.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author facun
 */
@Service
public class UsuarioServicio implements UserDetailsService {

    Usuario usuario;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ImagenRepositorio imagenRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    // --- Crear Usuario --- //
    public Usuario crearUsuario(String email, String password, String password2,
            String nombre, String apellido,
            MultipartFile archivo) throws Excepciones {

        validar(email, password, password2, nombre, apellido);

        usuario = new Usuario();

        usuario.setEmail(email);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setUltimaConexion(new Date());

        cargarImagen(archivo);// setea la imagen desde el metodo

        usuario.setRol(Rol.USER);
        usuario.setActivo(true);

        //atributos se activaran cuando se actualice a CLIENTE
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setTelefono(null);
        usuario.setDireccion(null);

        //atributos se activaran cuando se actualice a PROVEEDOR
        usuario.setCategoriaServicio(null);
        usuario.setComentarios(null);
        usuario.setServicios(null);

        return usuario;
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

    // --- Persistir Usuario --- //
    @Transactional
    public void persistirUsuario(String email, String password, String password2,
            String nombre, String apellido, 
            MultipartFile archivo) throws Excepciones {

        usuario = crearUsuario(email, password, password2, nombre, apellido,  archivo);
        usuarioRepositorio.save(usuario);
        System.out.println(" --- ");
        System.out.println("Usuario creado " + usuario.getEmail());
        System.out.println(" --- ");
    }

    public Usuario getOne(String id) {
        return usuarioRepositorio.getOne(id);
    }

    // --- UserDetails --- //
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

    // --- Validaciones Usuario --- //
    public void validar(String email, String password, String password2, String nombre,
            String apellido) throws Excepciones {

        if (nombre.isEmpty() || nombre == null) {
            throw new Excepciones("El nombre no puede ser nulo o estar vacio");
        }
        if (apellido.isEmpty() || apellido == null) {
            throw new Excepciones("El apellido no puede ser nulo o estar vacio");
        }

        if (email.isEmpty() || email == null) {
            throw new Excepciones("El email no puede ser nulo o estar vacio");
        }
        if (password.isEmpty() || password == null) {
            throw new Excepciones("El password no puede ser nulo o estar vacio");
        }
        if (password2.isEmpty() || password2 == null) {
            throw new Excepciones("El password2 no puede ser nulo o estar vacio");
        }

    }

    // --- Actualizar Usuario --- //
    @Transactional
    public Usuario actualizar(String id, String email, String nombre, String apellido, String telefono,
            MultipartFile archivo, @RequestParam(required = true) String password, String password2) throws Exception {

        validar(email, password, password2, nombre, apellido);

        
        System.out.println("TELEFONO---- "+telefono);
        
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {
            usuario = respuesta.get();
            // Verificar la contraseña antes de realizar las actualizaciones
            if (verificarContraseña(usuario, password)) {

                usuario.setNombre(nombre);
                usuario.setApellido(apellido);
                usuario.setEmail(email);

                usuario.setPassword(new BCryptPasswordEncoder().encode(password));

                cargarImagen(archivo);
                if(telefono.isEmpty()){
                    usuario.setTelefono(null);
                }
                
                if (usuario.getRol() != Rol.USER) {
                    usuario.setTelefono(telefono);
                } else {
                    usuario.setTelefono(null);
                }

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

    // --- Verificar Contraseña --- //
    private boolean verificarContraseña(Usuario usuario, String contraseña) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(contraseña, usuario.getPassword());
    }

//eliminar foto funcion btn
    @Transactional
    public Usuario eliminarImagenDeUsuario(String id) {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            usuario = respuesta.get();

            String idImagen = usuario.getImagen().getId();
            usuario.setImagen(null);
            usuarioRepositorio.save(usuario);

            imagenRepositorio.deleteById(idImagen);
            return usuario;
        }
        return null;
    }

    // --- Cambiar Alta --- //
    @Transactional
    public Usuario cambiarAlta(String id) {
        Optional<Usuario> resp = usuarioRepositorio.findById(id);

        if ((resp.isPresent() && usuario.getId().equals(id)) || (usuario.getRol().toString().equals("ADMIN"))) {
            usuario = resp.get();
            boolean b = usuario.getActivo();
            usuario.setActivo(!b);

            System.out.println("");
            System.out.println(usuario.getActivo());
            System.out.println("Alta servicio");
            System.out.println("");
            System.out.println("email: " + usuario.getEmail());
            System.out.println("");

            return usuario;

        } else {
            System.out.println("");
            System.out.println("No tienes Permisos");
        }

        return usuario;
    }

    // --- Listar Usuarios --- //
    @Transactional(readOnly = true)
    public List<Usuario> listarUsuarios() {
        List<Usuario> usuarios = new ArrayList();
        usuarios = usuarioRepositorio.findAll();
        return usuarios;
    }

    /*
        -------------------------------------------------------
        -------------------------------------------------------
                              PROVEEDOR
        -------------------------------------------------------
        -------------------------------------------------------
     */
    // --- Crear Proveedor --- //  
    @Transactional
    public void crearProveedor(String nombre, String apellido, String telefono,
            String email, String password, String password2, Categoria categoria, MultipartFile archivo) throws Excepciones, MiException {

        validarProveedor(nombre, apellido, telefono, email, password, password2);

        usuario = new Usuario();

        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setTelefono(telefono);
        usuario.setEmail(email);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setUltimaConexion(new Date());

        cargarImagen(archivo);

        usuario.setRol(Rol.PROVEEDOR);
        usuario.setActivo(true);

        usuario.setCategoriaServicio(categoria);
        usuario.setServicios(new ArrayList());

        usuarioRepositorio.save(usuario);

        System.out.println(" --- ");
        System.out.println("Proveedor creado " + usuario.getEmail());
        System.out.println(" --- ");

    }

    // --- Validaciondes Proveedor --- //
    public void validarProveedor(String nombre, String apellido, String telefono, String email, String password, String password2) throws MiException, Excepciones {

        if (nombre.isEmpty() || nombre == null) {
            throw new Excepciones("El nombre no puede ser nulo o estar vacio");
        }

        if (apellido.isEmpty() || apellido == null) {
            throw new Excepciones("El apellido no puede ser nulo o estar vacio");
        }

        if (telefono.isEmpty() || telefono == null) {
            throw new Excepciones("El Telefono no puede ser nulo o estar vacio");
        }

        if (email.isEmpty() || email == null) {
            throw new Excepciones("El email no puede ser nulo o estar vacio");
        }

        if (password.isEmpty() || password == null) {
            throw new Excepciones("El password no puede ser nulo o estar vacio");
        }
        if (password2.isEmpty() || password2 == null) {
            throw new Excepciones("El password2 no puede ser nulo o estar vacio");
        }

    }

    // --- Listas --- //
    @Transactional(readOnly = true)
    public List<Usuario> listarProveedores() {
        return usuarioRepositorio.listarProveedor();
    }

    // --- Listas por Categoria --- //
    @Transactional(readOnly = true)
    public List<Usuario> listarSalud() {
        return usuarioRepositorio.listarProveedoresPorCategoria(Rol.PROVEEDOR, Categoria.SALUD);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarElectricidad() {
        return usuarioRepositorio.listarProveedoresPorCategoria(Rol.PROVEEDOR, Categoria.ELECTRICIDAD);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarPlomeria() {
        return usuarioRepositorio.listarProveedoresPorCategoria(Rol.PROVEEDOR, Categoria.PLOMERIA);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarLimpieza() {
        return usuarioRepositorio.listarProveedoresPorCategoria(Rol.PROVEEDOR, Categoria.LIMPIEZA);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarJardineria() {
        return usuarioRepositorio.listarProveedoresPorCategoria(Rol.PROVEEDOR, Categoria.JARDINERIA);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarVarios() {
        return usuarioRepositorio.listarProveedoresPorCategoria(Rol.PROVEEDOR, Categoria.VARIOS);
    }

}
