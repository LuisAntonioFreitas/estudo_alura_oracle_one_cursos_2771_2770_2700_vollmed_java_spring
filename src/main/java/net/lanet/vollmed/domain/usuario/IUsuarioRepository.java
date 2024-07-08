package net.lanet.vollmed.domain.usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {

    List<Usuario> findByAtivoTrueOrderByNomeAsc();
    Page<Usuario> findByAtivoTrueOrderByNomeAsc(Pageable page);

    String queryFindAllFilter =
            """
            SELECT  u
            FROM    Usuario u
            WHERE   (u.nome LIKE %:search%
                    OR u.login LIKE %:search%
                    OR u.email LIKE %:search%)
            ORDER BY u.nome ASC
            """;
    @Query(queryFindAllFilter)
    List<Usuario> findAllFilter(String search);
    @Query(queryFindAllFilter)
    Page<Usuario> pageFindAllFilter(Pageable page, String search);

    String queryFindAllAtivoTrueFilter =
            """
            SELECT  u
            FROM    Usuario u
            WHERE   (u.ativo = true)
            AND     (u.nome LIKE %:search%
                    OR u.login LIKE %:search%
                    OR u.email LIKE %:search%)
            ORDER BY u.nome ASC
            """;
    @Query(queryFindAllAtivoTrueFilter)
    List<Usuario> findAllAtivoTrueFilter(String search);
    @Query(queryFindAllAtivoTrueFilter)
    Page<Usuario> pageFindAllAtivoTrueFilter(Pageable page, String search);

    Usuario findFirstTop1ByLoginOrEmail(String login, String email);
    UserDetails findFirstByLoginOrEmail(String login, String email);
    UserDetails findFirstById(Long id);
}
