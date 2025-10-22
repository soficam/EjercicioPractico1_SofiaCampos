/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package biblioteca.controller;

import biblioteca.domain.Queja;
import biblioteca.service.QuejaService;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContactoController {

    @Autowired
    private QuejaService quejaService;

    @Autowired
    private MessageSource messageSource;

    @ModelAttribute("tiposContacto")
    public List<String> tiposContacto() {
        // Debe coincidir con tu ENUM en BD: ('QUEJA','SUGERENCIA','CONSULTA')
        return Arrays.asList("CONSULTA", "SUGERENCIA", "QUEJA");
    }

    @GetMapping("/contacto")
    public String mostrarFormulario(Model model) {
        model.addAttribute("queja", new Queja());
        return "/contacto/form"; // crea /templates/contacto/form.html
    }

    @PostMapping("/contacto/enviar")
    public String enviar(@Valid Queja queja, RedirectAttributes ra) {
        quejaService.save(queja);
        ra.addFlashAttribute("todoOk", messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault()));
        return "redirect:/contacto";
    }
}

