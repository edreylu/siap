/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siap.siap.control;

import com.siap.siap.dao.RoleDAO;
import com.siap.siap.dao.UserDAO;
import com.siap.siap.entidad.Role;
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
public class RoleControl {

    @Autowired
    private UserDAO udao;
    @Autowired
    private RoleDAO rdao;
    List<Role> datos;
    Role role;
    List lista;
    int id;
//accesos al routing de las paginas en este caso la principal
    @GetMapping("/admin/roles/principal")
    public String listar(Model model) {
        datos = rdao.traeRegistros();
        model.addAttribute("lista", datos);
        return "/admin/roles/principal";
    }
//primer metodo para obtener agregar y segundo para hacer el guardado del registro.
    @GetMapping("/admin/roles/agregar")
    public String agregar(Model model) {
        model.addAttribute(new Role());
        return "/admin/roles/agregar";
    }
    
    @PostMapping(value = "/admin/roles/agregar")
    public String agregar(Role ro, RedirectAttributes redirectAttrs) {
        int valor = rdao.insertaRol(ro);
        if (valor >= 1) {
            System.out.println("se agrego registro: " + valor);
            redirectAttrs.addFlashAttribute("mensaje", "Agregado correctamente")
                    .addFlashAttribute("clase", "success");
        } else {
            System.err.println("no se agrego registro");
        }
        return "redirect:/admin/roles/principal";
    }
//primer metodo para obtener editar y segundo para hacer el editado del registro.
    
    @GetMapping(value = "/admin/roles/editar/{id}")
    public String editar(@PathVariable("id") int id, Model model) {
        role = rdao.rolEditar(id);
        model.addAttribute("role", role);
        return "/admin/roles/editar";
    }

    @PostMapping(value = "/admin/roles/update/{id}")
    public String editar(@PathVariable("id") int id, @Valid Role ro, RedirectAttributes redirectAttrs, Model model) {
        ro.setId(id);
        int valor = rdao.editaRol(ro);
        if (valor >= 1) {
            System.out.println("se edito registro: " + valor);
            redirectAttrs.addFlashAttribute("mensaje", "Editado correctamente")
                    .addFlashAttribute("clase", "success");
        } else {
            System.err.println("no se edito registro");
        }
        return "redirect:/admin/roles/principal";
    }

    @GetMapping("/admin/roles/eliminar/{id}")
    public String eliminar(@PathVariable("id") int id, RedirectAttributes redirectAttrs) {
        int valor = rdao.eliminaRol(id);
        if (valor >= 1) {
            System.out.println("se elimino registro: " + id);
            redirectAttrs.addFlashAttribute("mensaje", "Eliminado correctamente")
                    .addFlashAttribute("clase", "success");
        } else {
            System.err.println("no se elimino registro");
        }
        return "redirect:/admin/roles/principal";
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
