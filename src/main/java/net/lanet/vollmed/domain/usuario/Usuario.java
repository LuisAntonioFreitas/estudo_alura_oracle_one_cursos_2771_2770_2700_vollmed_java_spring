package net.lanet.vollmed.domain.usuario;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


@Entity(name = "Usuario")
@Table(name = "tb_usuarios", indexes = {
        @Index(name = "idx_login", columnList = "login"),
        @Index(name = "idx_email", columnList = "email")})
@Data // Contempla ( @Getter @Setter @EqualsAndHashCode @ToString )
@NoArgsConstructor @AllArgsConstructor
public class Usuario implements Serializable, UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable=false, unique=true)
    private Long id;

    @NotBlank(message = "Nome precisa ser preenchido.")
    @Size(max = 255, message = "Nome não pode conter mais do que 255 caracteres.")
    @Column(name="nome", nullable=false, length=255)
    private String nome;

    @NotBlank(message = "Login precisa ser preenchido.")
    @Size(max = 100, message = "Login não pode conter mais do que 100 caracteres.")
    @Column(name="login", nullable=false, unique=true, length=100)
    private String login;

    @NotBlank(message = "Email precisa ser preenchido.")
    @Size(max = 255, message = "Email não pode conter mais do que 255 caracteres.")
    @Email(message = "Email precisa ser preenchido corretamente.")
    @Column(name="email", nullable=false, unique=true, length=255)
    private String email;

    @JsonIgnore
    @NotBlank(message = "Senha precisa ser preenchida.")
    @Size(max = 255, message = "Senha não pode conter mais do que 255 caracteres.")
    @Column(name="senha", nullable=false, length=255)
    private String senha;

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


    public Usuario(UsuarioDtoCreateRequest data) {
        this.nome = data.nome();
        this.login = data.login();
        this.email = data.email();
        this.senha = data.senha();
    }


    public void update(UsuarioDtoUpdateRequest data) {
        if (data.nome() != null) {
            this.nome = data.nome();
        }
        if (data.login() != null) {
            this.login = data.login();
        }
        if (data.email() != null) {
            this.email = data.email();
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


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }
    @Override
    public String getPassword() { return this.senha; }
    @Override
    public String getUsername() { return this.login; }
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return this.ativo; }


}
