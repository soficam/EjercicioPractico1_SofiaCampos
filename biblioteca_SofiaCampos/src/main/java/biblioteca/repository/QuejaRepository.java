/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package biblioteca.repository;

import biblioteca.domain.Queja;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuejaRepository extends JpaRepository<Queja, Long> {

    //pra la bandeja de pendientes
    List<Queja> findByTratadoFalseOrderByCreatedAtDesc();

    //listado general ordenado
    List<Queja> findAllByOrderByCreatedAtDesc();
}

