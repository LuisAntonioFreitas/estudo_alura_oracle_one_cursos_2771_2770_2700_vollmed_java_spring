package net.lanet.vollmed.domain.medico;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import net.lanet.vollmed.domain.endereco.Endereco;
import net.lanet.vollmed.domain.endereco.EnderecoDtoView;
import net.lanet.vollmed.infra.utilities.DateTimeUtil;

@JsonPropertyOrder({"id"})
public record MedicoDtoView(
        Long id,
        String nome,
        String email,
        String telefone,
        String crm,
        Especialidade especialidade,
        Boolean ativo,
        String createdAt,
        String updatedAt,
        EnderecoDtoView endereco
) {
    public MedicoDtoView(Medico entity) {
        this(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                entity.getTelefone(),
                entity.getCrm(),
                entity.getEspecialidade(),
                entity.getAtivo(),
                entity.getCreatedAt().format(DateTimeUtil.formatter),
                entity.getUpdatedAt().format(DateTimeUtil.formatter),
                new EnderecoDtoView(entity.getEndereco())
        );
    }
}
