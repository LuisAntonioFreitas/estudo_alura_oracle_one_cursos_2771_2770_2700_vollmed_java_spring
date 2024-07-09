package net.lanet.vollmed.domain.endereco;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"logradouro", "numero", "complemento", "bairro", "cidade", "uf", "cep"})
public record EnderecoDtoView(
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String uf,
        String cep
) {
    public EnderecoDtoView(Endereco entity) {
        this(
                entity.getLogradouro(),
                entity.getNumero(),
                entity.getComplemento(),
                entity.getBairro(),
                entity.getCidade(),
                entity.getUf(),
                entity.getCep()
        );
    }
}
