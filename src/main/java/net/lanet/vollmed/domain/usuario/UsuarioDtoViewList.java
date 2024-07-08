package net.lanet.vollmed.domain.usuario;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import net.lanet.vollmed.infra.utilities.DateTimeUtil;

import java.time.LocalDateTime;

@JsonPropertyOrder({"id"})
public record UsuarioDtoViewList(
        Long id,
        String nome,
        String login,
        String email,
        Boolean ativo,
        String createdAt,
        String updatedAt
) {
    public UsuarioDtoViewList(Usuario entity) {
        this(
                entity.getId(),
                entity.getNome(),
                entity.getLogin(),
                entity.getEmail(),
                entity.getAtivo(),
                entity.getCreatedAt().format(DateTimeUtil.formatter),
                entity.getUpdatedAt().format(DateTimeUtil.formatter));
    }
}
