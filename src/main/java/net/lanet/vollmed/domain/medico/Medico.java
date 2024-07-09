package net.lanet.vollmed.domain.medico;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.lanet.vollmed.domain.endereco.Endereco;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;


@Entity(name = "Medico")
@Table(name = "tb_medicos", indexes = {
        @Index(name = "idx_crm", columnList = "crm"),
        @Index(name = "idx_especialidade", columnList = "especialidade")})
@Data // Contempla ( @Getter @Setter @EqualsAndHashCode @ToString )
@NoArgsConstructor @AllArgsConstructor
public class Medico implements Serializable {
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

    @Column(name="crm", nullable=false, unique=true, length=20)
    private String crm;

    @Enumerated(EnumType.STRING)
    @Column(name="especialidade", nullable=false)
    private Especialidade especialidade;

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


    public Medico(MedicoDtoCreateRequest data) {
        this.nome = data.nome();
        this.email = data.email();
        this.telefone = data.telefone();
        this.crm = data.crm();
        this.especialidade = Especialidade.valueOf(data.especialidade());
        this.endereco = new Endereco(data.endereco());
    }


    public void update(MedicoDtoUpdateRequest data) {
        if (data.nome() != null) {
            this.nome = data.nome();
        }
        if (data.email() != null) {
            this.email = data.email();
        }
        if (data.telefone() != null) {
            this.telefone = data.telefone();
        }
        if (data.especialidade() != null) {
            this.especialidade = Especialidade.valueOf(data.especialidade());
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

