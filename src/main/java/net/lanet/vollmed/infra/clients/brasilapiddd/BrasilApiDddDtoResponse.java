package net.lanet.vollmed.infra.clients.brasilapiddd;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BrasilApiDddDtoResponse(
        @JsonAlias("state")
        String estado,
        @JsonAlias("cities")
        List<String> cidades
) {
}
