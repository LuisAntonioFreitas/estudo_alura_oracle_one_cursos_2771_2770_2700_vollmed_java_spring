package net.lanet.vollmed.infra.clients.brasilapiddd;

import net.lanet.vollmed.infra.utilities.ConsumerApiUtil;
import net.lanet.vollmed.infra.utilities.ConvertsDataUtil;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
public class BrasilApiDddService implements IBrasilApiDddService {
    private final String URL_BASE = "https://brasilapi.com.br";

    @Override
    public Optional<BrasilApiDddDtoResponse> findDdd(String ddd) {
        String search = URLEncoder.encode(ddd, StandardCharsets.UTF_8).trim();
        String json = ConsumerApiUtil.getData(URL_BASE.concat("/api/ddd/v1/%s".formatted(search)));

        if (!json.isEmpty() && !json.trim().equalsIgnoreCase("")) {
            BrasilApiDddDtoResponse response =  ConvertsDataUtil.getDataJsonToClass(json, BrasilApiDddDtoResponse.class);
            return Optional.of(response);
        }
        return Optional.empty();
    }
}
