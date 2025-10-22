/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package biblioteca.repository;

import biblioteca.domain.Libro;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    List<Libro> findByDisponibleTrue();

    // libro nuevo según fecha de publicación
    Optional<Libro> findTopByOrderByFechaPublicacionDesc();

    // si no hay fecha_publicacion 
    Optional<Libro> findTopByOrderByIdDesc();
}


