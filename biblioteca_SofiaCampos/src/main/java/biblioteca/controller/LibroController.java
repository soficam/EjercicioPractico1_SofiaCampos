/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package biblioteca.controller;

import biblioteca.domain.Libro;
import biblioteca.service.CategoriaService;
import biblioteca.service.LibroService;
import jakarta.validation.Valid;
import java.util.Locale;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import java.beans.PropertyEditorSupport;


@Controller
@RequestMapping("/libro")
public class LibroController {

    @Autowired
    private LibroService libroService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/listado")
    public String listado(Model model) {
        var libros = libroService.getLibros(false); // false = incluir inactivos según tu patrón
        model.addAttribute("libros", libros);
        model.addAttribute("totalLibros", libros.size());
        var categorias = categoriaService.getCategorias(true);
        model.addAttribute("categorias", categorias);
        return "/libro/listado";
    }

    @GetMapping("/detalle/{idLibro}")
    public String detalle(@PathVariable("idLibro") Long idLibro, Model model, RedirectAttributes ra) {
        Optional<Libro> libroOpt = libroService.getLibro(idLibro);
        if (libroOpt.isEmpty()) {
            ra.addFlashAttribute("error", messageSource.getMessage("producto.error01", null, Locale.getDefault()));
            return "redirect:/libro/listado";
        }
        model.addAttribute("libro", libroOpt.get());
        return "/libro/detalle";
    }
    
    

    @GetMapping("/agregar")
    public String agregar(Model model) {
        // libro vacío + categorías para el <select>
        model.addAttribute("libro", new Libro());
        var categorias = categoriaService.getCategorias(true);
        model.addAttribute("categorias", categorias);
        return "/libro/modifica";
    }

    
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("libro") Libro libro,
                      BindingResult br,
                      @RequestParam(name = "categoria.id", required = false) Long categoriaId,
                      RedirectAttributes ra,
                      Model model) {

    // LOG de entrada útil
    System.out.println(">>> GUARDAR(POST) id=" + libro.getId()
        + ", titulo=" + libro.getTitulo()
        + ", categoriaId=" + categoriaId);

    if (br.hasErrors()) {
        System.out.println(">>> VALIDATION ERRORS:");
        br.getAllErrors().forEach(err -> System.out.println(" - " + err));
    }

    if (br.hasErrors() || categoriaId == null) {
        var categorias = categoriaService.getCategorias(true);
        model.addAttribute("categorias", categorias);
        if (categoriaId == null) model.addAttribute("error", "Debe seleccionar una categoría.");
        return "/libro/modifica";
    }

    var saved = libroService.saveOrUpdate(libro, categoriaId);
    System.out.println(">>> DESPUES DE SAVE id=" + saved.getId());

    ra.addFlashAttribute("todoOk", "Se realizó la actualización.");
    return "redirect:/libro/listado";
    }




    @GetMapping("/modificar/{idLibro}")
    public String modificar(@PathVariable("idLibro") Long idLibro,
                            Model model,
                            RedirectAttributes ra) {
        Optional<Libro> libroOpt = libroService.getLibro(idLibro);
        if (libroOpt.isEmpty()) {
            ra.addFlashAttribute("error",
                    messageSource.getMessage("libro.error01", null, Locale.getDefault()));
            return "redirect:/libro/listado";
        }
        model.addAttribute("libro", libroOpt.get());
        var categorias = categoriaService.getCategorias(true);
        model.addAttribute("categorias", categorias);
        return "/libro/modifica";
    }
    
    
    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Long idLibro, RedirectAttributes ra) {
    String titulo = "todoOk";
    String detalle = "mensaje.eliminado";
    try {
        libroService.delete(idLibro);
    } catch (IllegalArgumentException e) {
        titulo = "error";
        detalle = "libro.error01"; // crea esta clave en messages.properties
    } catch (IllegalStateException e) {
        titulo = "error";
        detalle = "libro.error02";
    } catch (Exception e) {
        titulo = "error";
        detalle = "libro.error03";
    }
    ra.addFlashAttribute(titulo, detalle);
    return "redirect:/libro/listado";
    }

    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Convierte "yyyy-MM-dd" -> java.sql.Date
        binder.registerCustomEditor(java.sql.Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (text == null || text.isBlank()) {
                    setValue(null);
                } else {
                    // valueOf espera exactamente "yyyy-[m]m-[d]d"
                    setValue(java.sql.Date.valueOf(text));
                }
            }
        });
    }
    
}

