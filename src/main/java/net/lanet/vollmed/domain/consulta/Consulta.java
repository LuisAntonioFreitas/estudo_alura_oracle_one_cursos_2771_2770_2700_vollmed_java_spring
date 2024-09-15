package net.lanet.vollmed.domain.consulta;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.lanet.vollmed.domain.medico.Medico;
import net.lanet.vollmed.domain.paciente.Paciente;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity(name = "Consulta")
@Table(name = "mv_consulta")
@Data // Contempla ( @Getter @Setter @EqualsAndHashCode @ToString )
@NoArgsConstructor @AllArgsConstructor
public class Consulta implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable=false, unique=true)
    private Long id;

    @Column(name="data", nullable=false)
    private LocalDateTime data;

    @Enumerated(EnumType.STRING)
    @Column(name = "motivo_cancelamento", length=100)
    private MotivoCancelamento motivoCancelamento;

    @Column(name="created_at", nullable=false, updatable=false)
    private LocalDateTime createdAt;

    @Column(name="updated_at", nullable=false)
    private LocalDateTime updatedAt;


    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Medico medico;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


    public Consulta(ConsultaDtoAgendarRequest data, Medico medico, Paciente paciente) {
        this.data = data.data();
        this.medico = medico;
        this.paciente = paciente;
    }


    public void cancelar(ConsultaDtoCancelarRequest data) {
        this.motivoCancelamento = MotivoCancelamento.valueOf(data.motivoCancelamento());
        onUpdate();
    }

}
