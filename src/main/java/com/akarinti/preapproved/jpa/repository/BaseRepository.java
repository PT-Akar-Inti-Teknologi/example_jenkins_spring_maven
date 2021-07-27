package com.akarinti.preapproved.jpa.repository;

import com.akarinti.preapproved.jpa.entity.Base;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T extends Base> extends JpaRepository<T, Integer> {
    T findBySecureId(String secureId);
    Optional<T> findById(Integer id);

    @Transactional
    default void deleteRow(T entity) {
        if (null != entity) {
            this.delete(entity);
        }
    }

    @Transactional
    default void deleteRow(Iterable<? extends T> entities) {
        if (null != entities) {
            for (T entity : entities) {
                deleteRow(entity);
            }
        }
    }
}

