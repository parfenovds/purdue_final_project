package com.parfenov.purdue_final.service;

import com.parfenov.purdue_final.dto.BaseDTO;
import java.util.List;

public interface BaseService<D extends BaseDTO> {
  D findById(Long id);

  void delete(Long id);

  D update(Long id, D entity);

  D save(D entity);

  List<D> findAll();
}
