package net.lanet.vollmed.domain.syssql;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

//@Hidden // Swagger
@JsonIgnoreProperties(ignoreUnknown = true)
public record SysSqlDtoRequest(
        @NotBlank
        String sql,
        @Nullable
        String result
) {
}
