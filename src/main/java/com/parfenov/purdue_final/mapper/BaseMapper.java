package com.parfenov.purdue_final.mapper;

import java.util.List;

public interface BaseMapper<E, D> {
  D toDto(E entity);

  E toEntity(D dto);

  List<D> toDtoList(List<E> entities);
}
