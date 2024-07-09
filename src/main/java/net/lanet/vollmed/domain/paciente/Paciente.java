package net.lanet.vollmed.domain.paciente;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.lanet.vollmed.domain.endereco.Endereco;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;


@Entity(name = "Paciente")
@Table(name = "tb_pacientes", indexes = {
        @Index(name = "idx_cpf", columnList = "cpf")})
@Data // Contempla ( @Getter @Setter @EqualsAndHashCode @ToString )
@NoArgsConstructor @AllArgsConstructor
public class Paciente implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable=false, unique=true)
    private Long id;

    @Column(name="nome", nullable=false, length=255)
    private String nome;

    @Column(name="email", nullable=false, unique=true, length=255)
    private String email;

    @Column(name="telefone", nullable=false, length=50)
    private String telefone;

    @Column(name="cpf", nullable=false, unique=true, length=20)
    private String cpf;

    @Embedded
    private Endereco endereco;

    @Column(name="ativo", nullable=false)
    private Boolean ativo;

    @Column(name="created_at", nullable=false, updatable=false)
    private LocalDateTime createdAt;

    @Column(name="updated_at", nullable=false)
    private LocalDateTime updatedAt;


    @PrePersist
    protected void onCreate() {
        this.ativo = true;

        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


    public Paciente(PacienteDtoCreateRequest data) {
        this.nome = data.nome();
        this.email = data.email();
        this.telefone = data.telefone();
        this.cpf = data.cpf();
        this.endereco = new Endereco(data.endereco());
    }


    public void update(PacienteDtoUpdateRequest data) {
        if (data.nome() != null) {
            this.nome = data.nome();
        }
        if (data.email() != null) {
            this.email = data.email();
        }
        if (data.telefone() != null) {
            this.telefone = data.telefone();
        }
        if (data.endereco() != null) {
            this.endereco.update(data.endereco());
        }
        onUpdate();
    }
    public void delete() {
        this.ativo = false;
        onUpdate();
    }
    public void ativa() {
        this.ativo = true;
        onUpdate();
    }



}


