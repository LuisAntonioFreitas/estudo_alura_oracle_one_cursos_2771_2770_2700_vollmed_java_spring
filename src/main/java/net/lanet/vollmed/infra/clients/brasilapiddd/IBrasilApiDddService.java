package net.lanet.vollmed.infra.clients.brasilapiddd;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface IBrasilApiDddService {
    Optional<BrasilApiDddDtoResponse> findDdd(String ddd);
}
