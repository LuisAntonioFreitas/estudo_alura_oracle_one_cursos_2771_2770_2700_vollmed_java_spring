package net.lanet.vollmed.controller.clients;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.lanet.vollmed.infra.clients.brasilapiddd.BrasilApiDddDtoResponse;
import net.lanet.vollmed.infra.clients.brasilapiddd.IBrasilApiDddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Tag(name = "Ddd") // Swagger
@RequestMapping(path = "${api.config.path}/ddd")
public class BrasilApiDddController {
    @Autowired
    @Qualifier("brasilApiDddService")
    private IBrasilApiDddService service;

    @GetMapping(path = {"/{ddd}"})
    public ResponseEntity<Object> findCep(@PathVariable String ddd) {
        Optional<BrasilApiDddDtoResponse> response = service.findDdd(ddd);
        if (response.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
