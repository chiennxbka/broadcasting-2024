package com.mintpot.broadcasting.repository.specs;

import com.mintpot.broadcasting.common.enums.SearchOperation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SearchCriteria {
  private String key;
  private Object value;
  private SearchOperation operation;
}
