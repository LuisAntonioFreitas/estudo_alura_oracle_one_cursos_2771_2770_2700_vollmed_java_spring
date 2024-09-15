package net.lanet.vollmed.domain.medico;

import net.lanet.vollmed.infra.shared.JpaRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IMedicoRepository extends JpaRepositoryCustom.MethodsStandard<Medico> {
    List<Medico> findByAtivoTrueOrderByNomeAsc();
    Page<Medico> findByAtivoTrueOrderByNomeAsc(Pageable page);

    String queryFindAllFilter =
            """
            SELECT  m
            FROM    Medico m
            WHERE   (m.nome LIKE %:search%
                    OR m.crm LIKE %:search%
                    OR m.especialidade LIKE %:search%
                    OR m.endereco.cidade LIKE %:search%
                    OR m.endereco.uf LIKE %:search%)
            ORDER BY m.nome ASC
            """;
    @Query(queryFindAllFilter)
    List<Medico> findAllFilter(String search);
    @Query(queryFindAllFilter)
    Page<Medico> pageFindAllFilter(Pageable page, String search);

    String queryFindAllAtivoTrueFilter =
            """
            SELECT  m
            FROM    Medico m
            WHERE   (m.ativo = true)
            AND     (m.nome LIKE %:search%
                    OR m.crm LIKE %:search%
                    OR m.especialidade LIKE %:search%
                    OR m.endereco.cidade LIKE %:search%
                    OR m.endereco.uf LIKE %:search%)
            ORDER BY m.nome ASC
            """;
    @Query(queryFindAllAtivoTrueFilter)
    List<Medico> findAllAtivoTrueFilter(String search);
    @Query(queryFindAllAtivoTrueFilter)
    Page<Medico> pageFindAllAtivoTrueFilter(Pageable page, String search);

    @Query("""
            SELECT  m.ativo
            FROM    Medico m
            WHERE   (m.id = :id)
            """)
    Boolean findAtivoById(Long id);

    @Query(value = """
            SELECT  m 
            FROM    Medico m
            WHERE   (m.ativo = true)
            AND     (m.especialidade = :especialidade)
            AND     (m.id NOT IN (
                        SELECT  c.medico.id 
                        FROM    Consulta c
                        WHERE   (c.data = :data)
                        AND     (c.motivoCancelamento is null)
                        )
                    )
        """)
    List<Medico> sortearMedicoAleatorioLivreNaData(Especialidade especialidade, LocalDateTime data);
}
