package com.mintpot.broadcasting.service.generic;

import java.util.ArrayList;
import java.util.List;

public interface Service<E, F> {
  default Long insert(F request){
    return null;
  }

  default void update(F request) {
  }

  default List<E> findAll() {
    return new ArrayList<>();
  }


  default E findById(Long id) {
    return null;
  }

  default void remove(Long id) {
  }
}
