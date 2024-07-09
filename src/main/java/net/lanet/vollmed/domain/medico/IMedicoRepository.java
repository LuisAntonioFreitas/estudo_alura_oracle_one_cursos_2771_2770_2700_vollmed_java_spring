package net.lanet.vollmed.domain.medico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMedicoRepository extends JpaRepository<Medico, Long> {
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
}
