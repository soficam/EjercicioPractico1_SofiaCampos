/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package biblioteca.service;

import biblioteca.domain.Queja;
import biblioteca.repository.QuejaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuejaService {

    @Autowired
    private QuejaRepository quejaRepository;

    @Transactional(readOnly = true)
    public List<Queja> getTodas(boolean soloPendientes) {
        return soloPendientes ? quejaRepository.findByTratadoFalseOrderByCreatedAtDesc()
                              : quejaRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public Optional<Queja> getQueja(Long id) {
        return quejaRepository.findById(id);
    }

    @Transactional
    public Queja save(Queja q) {
        return quejaRepository.save(q);
    }

    @Transactional
    public void marcarTratado(Long id) {
        Queja q = quejaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La queja con ID " + id + " no existe."));
        q.setTratado(true);
        quejaRepository.save(q);
    }

    @Transactional
    public void delete(Long id) {
        if (!quejaRepository.existsById(id)) {
            throw new IllegalArgumentException("La queja con ID " + id + " no existe.");
        }
        try {
            quejaRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar el registro. Está asociado a otros datos.", e);
        }
    }
}
