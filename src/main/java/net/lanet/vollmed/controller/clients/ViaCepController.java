package net.lanet.vollmed.controller.clients;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lanet.vollmed.infra.clients.viacep.ViaCepDtoResponse;
import net.lanet.vollmed.infra.clients.viacep.IViaCepService;
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
@Tag(name = "Cep") // Swagger
@SecurityRequirement(name = "bearer-key") // Swagger
@RequestMapping(path = "${api.config.path}/cep")
public class ViaCepController {
    @Autowired
    @Qualifier("viaCepService")
    private IViaCepService service;

    @Operation(summary = "consulta Cep na ViaCEP") // Swagger
    @GetMapping(path = {"/{cep}"})
    public ResponseEntity<Object> findCep(@PathVariable String cep) {
        Optional<ViaCepDtoResponse> response = service.findCep(cep);
        if (response.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}