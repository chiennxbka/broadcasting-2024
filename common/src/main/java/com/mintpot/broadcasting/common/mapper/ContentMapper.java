package com.mintpot.broadcasting.common.mapper;

import com.mintpot.broadcasting.common.entities.Content;
import com.mintpot.broadcasting.common.response.ContentResponse;
import com.mintpot.broadcasting.common.response.ResourceResponse;
import org.mapstruct.Mapper;

@Mapper
public interface ContentMapper {

  default ContentResponse toDto(Content content) {
    return null;
  }

    default ResourceResponse toResources(Content content) {
        return null;
    }
}
