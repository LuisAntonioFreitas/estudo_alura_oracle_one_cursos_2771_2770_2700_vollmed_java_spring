package net.lanet.vollmed.domain.endereco;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data // Contempla ( @Getter @Setter @EqualsAndHashCode @ToString )
@NoArgsConstructor @AllArgsConstructor
public class Endereco {

    @Column(name="logradouro", nullable=false, length=100)
    private String logradouro;

    @Column(name="numero", length=20)
    private String numero;

    @Column(name="complemento", length=100)
    private String complemento;

    @Column(name="bairro", nullable=false, length=100)
    private String bairro;

    @Column(name="cidade", nullable=false, length=100)
    private String cidade;

    @Column(name="uf", nullable=false, length=2)
    private String uf;

    @Column(name="cep", nullable=false, length=10)
    private String cep;


    public Endereco(EnderecoDtoCreateRequest data) {
        this.logradouro = data.logradouro();
        this.numero = data.numero();
        this.complemento = data.complemento();
        this.bairro = data.bairro();
        this.cidade = data.cidade();
        this.uf = data.uf();
        this.cep = data.cep();
    }


    public void update(EnderecoDtoUpdateRequest data) {
        if (data.logradouro() != null) {
            this.logradouro = data.logradouro();
        }
        if (data.numero() != null) {
            this.numero = data.numero();
        }
        if (data.complemento() != null) {
            this.complemento = data.complemento();
        }
        if (data.bairro() != null) {
            this.bairro = data.bairro();
        }
        if (data.cidade() != null) {
            this.cidade = data.cidade();
        }
        if (data.uf() != null) {
            this.uf = data.uf();
        }
        if (data.cep() != null) {
            this.cep = data.cep();
        }
    }

}
