package com.siap.siap.control;

import com.siap.siap.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class AppControl {

    @Autowired
    private UserDAO udao;
    //accesos a aplicacion desde el login hacia el menu cuando si accede y 403 cuando no hay permisos.
    @GetMapping("/login")
    public String index() {
        return "login";
    }

    @GetMapping("/menu")
    public String login() {
        return "menu";
    }

    @GetMapping("/403")
    public String Error403() {
        return "403";
    }
//asigna datos de sesion
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
