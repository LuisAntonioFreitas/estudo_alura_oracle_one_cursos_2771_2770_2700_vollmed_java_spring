package net.lanet.vollmed.infra.clients.viacep;

import java.util.Optional;

public interface IViaCepService {
    Optional<ViaCepDtoResponse> findCep(String cep);
}
