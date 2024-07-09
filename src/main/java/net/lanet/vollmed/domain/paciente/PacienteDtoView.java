package net.lanet.vollmed.domain.paciente;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import net.lanet.vollmed.domain.endereco.Endereco;
import net.lanet.vollmed.domain.endereco.EnderecoDtoView;
import net.lanet.vollmed.infra.utilities.DateTimeUtil;

@JsonPropertyOrder({"id"})
public record PacienteDtoView(
        Long id,
        String nome,
        String email,
        String telefone,
        String cpf,
        Boolean ativo,
        String createdAt,
        String updatedAt,
        EnderecoDtoView endereco
) {
    public PacienteDtoView(Paciente entity) {
        this(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                entity.getTelefone(),
                entity.getCpf(),
                entity.getAtivo(),
                entity.getCreatedAt().format(DateTimeUtil.formatter),
                entity.getUpdatedAt().format(DateTimeUtil.formatter),
                new EnderecoDtoView(entity.getEndereco())
        );
    }
}
