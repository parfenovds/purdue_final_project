package com.parfenov.purdue_final.repository;

import com.parfenov.purdue_final.entity.AbstractEntity;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<E extends AbstractEntity> extends CrudRepository<E, Long> {

  default E update(E entity) {
    findById(entity.getId()).orElseThrow(() -> new RuntimeException("Entity not found"));
    return save(entity);
  }

  List<E> findAll();
}
