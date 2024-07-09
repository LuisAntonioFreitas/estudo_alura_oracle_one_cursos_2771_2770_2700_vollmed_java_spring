package net.lanet.vollmed.domain.paciente;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import net.lanet.vollmed.domain.endereco.Endereco;
import net.lanet.vollmed.domain.endereco.EnderecoDtoView;
import net.lanet.vollmed.infra.utilities.DateTimeUtil;

@JsonPropertyOrder({"id"})
public record PacienteDtoViewList(
        Long id,
        String nome,
        String email,
        String telefone,
        String cpf,
        Boolean ativo,
        EnderecoDtoView endereco
) {
    public PacienteDtoViewList(Paciente entity) {
        this(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                entity.getTelefone(),
                entity.getCpf(),
                entity.getAtivo(),
                new EnderecoDtoView(entity.getEndereco())
        );
    }
}