package com.parfenov.purdue_final.service;

import com.parfenov.purdue_final.dto.BaseDTO;
import com.parfenov.purdue_final.entity.AbstractEntity;
import com.parfenov.purdue_final.exception.NotFoundException;
import com.parfenov.purdue_final.mapper.BaseMapper;
import com.parfenov.purdue_final.repository.BaseRepository;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Transactional;


@Log4j2
public abstract class BaseServiceImpl<E extends AbstractEntity, D extends BaseDTO> implements BaseService<D> {

  private final BaseRepository<E> baseRepository;
  private final Class<E> entityClass;
  private final BaseMapper<E, D> baseMapper;

  protected BaseServiceImpl(
      BaseRepository<E> baseRepository,
      Class<E> entityClass,
      BaseMapper<E, D> baseMapper) {
    this.baseRepository = baseRepository;
    this.entityClass = entityClass;
    this.baseMapper = baseMapper;
  }

  @Transactional(readOnly = true)
  @Override
  public D findById(Long id) {
      E entity = baseRepository.findById(id).orElseThrow(() -> new NotFoundException(entityClass, id));
      return baseMapper.toDto(entity);
  }

  @Transactional
  @Override
  public void delete(Long id) {
      baseRepository.deleteById(id);
  }

  @Transactional
  @Override
  public D update(Long id, D dto) {
      E found = baseRepository.findById(id).orElseThrow(() -> new NotFoundException(entityClass, id));
      E entity = baseMapper.toEntity(dto);
      E updated = baseRepository.update(entity);
      return baseMapper.toDto(updated);
  }

  @Transactional
  @Override
  public D save(D dto) {
      E entity = baseMapper.toEntity(dto);
      E saved = baseRepository.save(entity);
      return baseMapper.toDto(saved);
  }

  @Transactional(readOnly = true)
  @Override
  public List<D> findAll() {
      List<E> entities = baseRepository.findAll();
      return baseMapper.toDtoList(entities);
  }
}
