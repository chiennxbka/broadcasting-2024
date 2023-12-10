package com.mintpot.broadcasting.common.response;

import com.mintpot.broadcasting.common.enums.CategoryFile;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
public class ContentResponse {

  private Long id;

  private String name;

  @Enumerated(EnumType.STRING)
  private CategoryFile type;

  private String uri;

  private long size;
}
