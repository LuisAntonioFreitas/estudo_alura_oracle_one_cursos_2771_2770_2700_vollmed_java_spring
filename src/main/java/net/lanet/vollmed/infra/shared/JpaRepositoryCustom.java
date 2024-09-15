package net.lanet.vollmed.infra.shared;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public class JpaRepositoryCustom {
    public interface MethodsStandard<T> extends JpaRepository<T, Long> {
    }

    public static <T> T findEntityNotNull(Long id, T entity, MethodsStandard<T> repository, Object[] item) {
        String message = String.format("%s não foi encontrad%s", item[1], item[0]);
        if (id == null && entity != null) { return entity; }
        if (id == null) { throw new EntityNotFoundException(message); }
        Optional<T> optional = repository.findById(id);
        if (optional.isEmpty()) {
            throw new EntityNotFoundException(message);
        }
        return optional.get();
    }
    public static <T> T findEntityNull(Long id, T entity, MethodsStandard<T> repository, Object[] item) {
        if (id == null && entity == null) { return null; }
        return findEntityNotNull(id, entity, repository, item);
    }
}
