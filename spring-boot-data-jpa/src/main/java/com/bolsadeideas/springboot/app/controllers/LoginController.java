package com.bolsadeideas.springboot.app.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
    // captura el flag "error" en la barra de nav
    // o la flag de "logout" cuando se cierra sesión
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model, Principal principal, RedirectAttributes flash) {
        if (principal != null) {
            flash.addFlashAttribute("info", "Ya ha iniciado sesión anteriormente");
            return "redirect:/";
        }
        // maneja errores de autenticación
        if (error != null) {
            model.addAttribute("error", "error el el login: Nombre de usuario o contraseña no válido.");
        }
        if (logout != null) {
            model.addAttribute("success", "Ha cerrado sesión con éxito");
        }
        return "login";
    }
}
