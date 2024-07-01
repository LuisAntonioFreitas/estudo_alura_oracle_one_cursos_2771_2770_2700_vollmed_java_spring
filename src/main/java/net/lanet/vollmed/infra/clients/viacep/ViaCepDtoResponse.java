package net.lanet.vollmed.infra.clients.viacep;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ViaCepDtoResponse(
        @JsonAlias("cep")
        String cep,
        @JsonAlias("logradouro")
        String logradouro,
        @JsonAlias("complemento")
        String complemento,
        @JsonAlias("unidade")
        String unidade,
        @JsonAlias("bairro")
        String bairro,
        @JsonAlias("localidade")
        String localidade,
        @JsonAlias("uf")
        String uf,
        @JsonAlias("ibge")
        String ibge,
        @JsonAlias("gia")
        String gia,
        @JsonAlias("ddd")
        String ddd,
        @JsonAlias("siafi")
        String siafi
) {
}
