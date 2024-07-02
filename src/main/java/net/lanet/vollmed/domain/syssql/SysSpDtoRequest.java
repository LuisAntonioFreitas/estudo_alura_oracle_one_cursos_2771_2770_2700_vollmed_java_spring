package net.lanet.vollmed.domain.syssql;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

import java.util.Map;

//@Hidden // Swagger
@JsonIgnoreProperties(ignoreUnknown = true)
public record SysSpDtoRequest(
        @NotBlank
        String sp,
        @Nullable
        Map<String, Object> param,
        @Nullable
        String result
) {
}
