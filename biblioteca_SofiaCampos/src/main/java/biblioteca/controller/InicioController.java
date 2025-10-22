/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package biblioteca.controller;

import biblioteca.domain.Libro;
import biblioteca.service.LibroService;
import java.util.Locale;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {

    @Autowired
    private LibroService libroService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping({"/", "/inicio"})
    public String inicio(Model model) {
        // Toma el libro más reciente como "nuevo libro destacado"
        Optional<Libro> libroNuevo = libroService.buscarMasReciente(); // implementa en tu service/DAO
        model.addAttribute("libroNuevo", libroNuevo.orElse(null));
        return "index"; // tu index.html que carga fragmentos
    }
}
