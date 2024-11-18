package com.parfenov.purdue_final.exception;

public class NotFoundException extends RuntimeException {

  public NotFoundException(Class<?> clazz, Long id) {
    super(String.format(
        "%s not found by id = %s",
        clazz.getSimpleName(),
        id
    ));
  }

  public NotFoundException(Class<?> clazz, Object object) {
    super(String.format(
        "%s not found by %s",
        clazz.getSimpleName(),
        object.toString()
    ));
  }

  public NotFoundException(Class<?> clazz, Long id, Object object) {
    super(String.format(
        "%s not found by %s and %s",
        clazz.getSimpleName(),
        id,
        object
    ));
  }

  public NotFoundException(String message) {
    super(message);
  }
}