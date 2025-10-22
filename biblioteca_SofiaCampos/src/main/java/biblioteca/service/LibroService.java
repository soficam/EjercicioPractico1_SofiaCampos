/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package biblioteca.service;

import biblioteca.domain.Libro;
import biblioteca.repository.LibroRepository;
import biblioteca.repository.CategoriaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository; 


    @Transactional(readOnly = true)
    public List<Libro> getLibros(boolean soloDisponibles) {
        return soloDisponibles ? libroRepository.findByDisponibleTrue()
                               : libroRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Libro> getLibro(Long idLibro) {
        return libroRepository.findById(idLibro);
    }

    /*
    @Transactional
    public Libro save(Libro libro) {
        return libroRepository.save(libro);
    }
    */
    
    @Transactional
    public Libro saveOrUpdate(Libro src, Long categoriaId) {
        if (categoriaId != null) {
            src.setCategoria(
                categoriaRepository.findById(categoriaId)
                    .orElseThrow(() -> new IllegalArgumentException("Categoría inválida"))
            );
        }

        if (src.getId() != null) {
            Libro dst = libroRepository.findById(src.getId())
                .orElseThrow(() -> new IllegalArgumentException("El libro no existe"));

            dst.setTitulo(src.getTitulo());
            dst.setAutor(src.getAutor());
            dst.setDescripcion(src.getDescripcion());
            dst.setIsbn(src.getIsbn());
            dst.setFechaPublicacion(src.getFechaPublicacion());
            dst.setCategoria(src.getCategoria());
            dst.setDisponible(src.isDisponible());
            dst.setPrecio(src.getPrecio());

            Libro updated = libroRepository.saveAndFlush(dst);
            System.out.println(">>> SQL UPDATE OK id=" + updated.getId());
            return updated;
        }

        Libro created = libroRepository.saveAndFlush(src);
        System.out.println(">>> SQL INSERT OK id=" + created.getId());
        return created;
    }

    
    

    @Transactional
    public void delete(Long idLibro) {
        if (!libroRepository.existsById(idLibro)) {
            throw new IllegalArgumentException("El libro con ID " + idLibro + " no existe.");
        }
        try {
            libroRepository.deleteById(idLibro);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar el libro. Tiene datos asociados.", e);
        }
    }

    
    /**
     * Devuelve el libro más reciente por fecha_publicacion
     */
    @Transactional(readOnly = true)
    public Optional<Libro> buscarMasReciente() {
        Optional<Libro> porFecha = libroRepository.findTopByOrderByFechaPublicacionDesc();
        if (porFecha.isPresent()) return porFecha;
        return libroRepository.findTopByOrderByIdDesc();
    }

}