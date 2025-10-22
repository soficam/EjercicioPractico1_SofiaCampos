package biblioteca.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.sql.Timestamp;
import lombok.Data;

@Data
@Entity
@Table(name = "queja")
public class Queja implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_cliente", length = 150)
    @Size(max = 150, message = "El nombre no puede tener más de 150 caracteres.")
    private String nombreCliente;

    @Column(length = 200)
    @Email(message = "Debe ingresar un correo válido.")
    private String email;

    @Column(length = 30)
    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private Tipo tipo = Tipo.CONSULTA;

    @Column(length = 200)
    private String asunto;

    @Column(columnDefinition = "TEXT", nullable = false)
    @NotBlank(message = "El mensaje no puede estar vacío.")
    private String mensaje;

    @Column(nullable = false)
    private boolean tratado = false;

    // <- Deja que MySQL ponga el timestamp por defecto
    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    public enum Tipo {
        QUEJA, SUGERENCIA, CONSULTA
    }
}
