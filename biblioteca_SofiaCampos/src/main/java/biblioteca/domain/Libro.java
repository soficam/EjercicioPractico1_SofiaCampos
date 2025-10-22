/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package biblioteca.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Entity
@Table(name = "libro")
public class Libro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "El título no puede estar vacío.")
    @Size(max = 255, message = "El título no puede tener más de 255 caracteres.")
    private String titulo;

    @Column(nullable = false, length = 200)
    @NotBlank(message = "El autor no puede estar vacío.")
    @Size(max = 200, message = "El autor no puede tener más de 200 caracteres.")
    private String autor;

    @Column(length = 20)
    private String isbn;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(name = "fecha_publicacion")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaPublicacion;

    @Column(nullable = false)
    private boolean disponible = true;

    @Column(precision = 10, scale = 2)
    @DecimalMin(value = "0.00", inclusive = true, message = "El precio debe ser mayor o igual a 0.")
    private BigDecimal precio;

    // --- IMPORTANTE: dejamos que MySQL maneje estos valores ---
    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Timestamp updatedAt;
}


