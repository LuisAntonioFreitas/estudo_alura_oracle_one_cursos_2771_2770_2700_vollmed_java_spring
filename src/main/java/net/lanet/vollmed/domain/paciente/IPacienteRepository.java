package net.lanet.vollmed.domain.paciente;

import net.lanet.vollmed.infra.shared.JpaRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPacienteRepository extends JpaRepositoryCustom.MethodsStandard<Paciente> {
    List<Paciente> findByAtivoTrueOrderByNomeAsc();
    Page<Paciente> findByAtivoTrueOrderByNomeAsc(Pageable page);

    String queryFindAllFilter =
            """
            SELECT  p
            FROM    Paciente p
            WHERE   (p.nome LIKE %:search%
                    OR p.cpf LIKE %:search%
                    OR p.endereco.cidade LIKE %:search%
                    OR p.endereco.uf LIKE %:search%)
            ORDER BY p.nome ASC
            """;
    @Query(queryFindAllFilter)
    List<Paciente> findAllFilter(String search);
    @Query(queryFindAllFilter)
    Page<Paciente> pageFindAllFilter(Pageable page, String search);

    String queryFindAllAtivoTrueFilter =
            """
            SELECT  p
            FROM    Paciente p
            WHERE   (p.ativo = true)
            AND     (p.nome LIKE %:search%
                    OR p.cpf LIKE %:search%
                    OR p.endereco.cidade LIKE %:search%
                    OR p.endereco.uf LIKE %:search%)
            ORDER BY p.nome ASC
            """;
    @Query(queryFindAllAtivoTrueFilter)
    List<Paciente> findAllAtivoTrueFilter(String search);
    @Query(queryFindAllAtivoTrueFilter)
    Page<Paciente> pageFindAllAtivoTrueFilter(Pageable page, String search);

    @Query("""
            SELECT  p.ativo
            FROM    Paciente p
            WHERE   (p.id = :id)
            """)
    Boolean findAtivoById(Long id);
}
