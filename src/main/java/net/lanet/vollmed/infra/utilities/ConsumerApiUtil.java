package net.lanet.vollmed.infra.utilities;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class ConsumerApiUtil {

    public static String getData(String address) {

        URI uri = URI.create(address);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .build();
        HttpResponse<String> response = null;
        try {
            response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 404) {
                throw new EntityNotFoundException("");
            }
            if (response.statusCode() == 400) {
                throw new HttpMessageNotReadableException("");
            }
            if (response.statusCode() == 500) {
                throw new RuntimeException("");
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        String json = "";
        if (response.statusCode() != 500) {
            json = response.body();
        }
        return json;
    }
}
