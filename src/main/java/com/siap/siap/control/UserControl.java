/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siap.siap.control;

import com.siap.siap.dao.RoleDAO;
import com.siap.siap.dao.UserDAO;
import com.siap.siap.entidad.Role;
import com.siap.siap.entidad.User;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author edreylu
 */
@Controller
public class UserControl {

    @Autowired
    private UserDAO udao;
    @Autowired
    private RoleDAO rdao;
    List<User> datos;
    List<Role> datosRoles;
    User user;
    List lista;
    int id;
//accesos al routing de las paginas en este caso la principal
    @GetMapping("/admin/usuarios/principal")
    public String listar(Model model) {
        datos = udao.traeRegistros();
        model.addAttribute("lista", datos);
        return "/admin/usuarios/principal";
    }
//primer metodo para obtener agregar y segundo para hacer el guardado del registro.
    @GetMapping("/admin/usuarios/agregar")
    public String agregar(Model model) {
        datosRoles = rdao.traeRegistros();
        model.addAttribute("listaRoles", datosRoles);
        model.addAttribute(new User());
        return "/admin/usuarios/agregar";
    }

    @PostMapping(value = "/admin/usuarios/agregar")
    public String agregar(User us, RedirectAttributes redirectAttrs) {
        int valor = udao.insertaUsuario(us);
        if (valor >= 1) {
            System.out.println("se agrego registro: " + valor);
            redirectAttrs.addFlashAttribute("mensaje", "Agregado correctamente")
                    .addFlashAttribute("clase", "success");
        } else {
            System.err.println("no se agrego registro");
        }
        return "redirect:/admin/usuarios/principal";
    }
//primer metodo para obtener editar y segundo para hacer el editado del registro.
    @GetMapping(value = "/admin/usuarios/editar/{id}")
    public String editar(@PathVariable("id") int id, Model model) {
        user = udao.usuarioEditar(id);
        datosRoles = rdao.traeRegistros();
        model.addAttribute("listaRoles", datosRoles);
        model.addAttribute("user", user);
        return "/admin/usuarios/editar";
    }

    @PostMapping(value = "/admin/usuarios/update/{id}")
    public String editar(@PathVariable("id") int id, @Valid User us, RedirectAttributes redirectAttrs, Model model) {
        us.setId(id);
        int valor = udao.editaUsuario(us);
        if (valor >= 1) {
            System.out.println("se edito registro: " + valor);
            redirectAttrs.addFlashAttribute("mensaje", "Editado correctamente")
                    .addFlashAttribute("clase", "success");
        } else {
            System.err.println("no se edito registro");
        }
        return "redirect:/admin/usuarios/principal";
    }

    @GetMapping("/admin/usuarios/eliminar/{id}")
    public String eliminar(@PathVariable("id") int id, RedirectAttributes redirectAttrs) {
        int valor = udao.eliminaUsuario(id);
        if (valor >= 1) {
            System.out.println("se elimino registro: " + id);
            redirectAttrs.addFlashAttribute("mensaje", "Eliminado correctamente")
                    .addFlashAttribute("clase", "success");
        } else {
            System.err.println("no se elimino registro");
        }
        return "redirect:/admin/usuarios/principal";
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        String userName;
        String nombre = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = null;
        if (principal instanceof UserDetails) {
            userDetails = (UserDetails) principal;
        }
        if (userDetails == null) {
            userName = "";
        } else {
            userName = userDetails.getUsername();
            nombre = udao.nombreUsuario(userName);
        }
        model.addAttribute("userName", userName);
        model.addAttribute("nombre", nombre);
    }
}
