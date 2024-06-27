package net.lanet.vollmed.infra.clients.brasilapiddd;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public record BrasilApiDddDtoResponse(
        @JsonAlias("state")
        String estado,
        @JsonAlias("cities")
        List<String> cidades
) {
}
