package com.mintpot.broadcasting.service.mapper;

import com.mintpot.broadcasting.common.entities.Content;
import com.mintpot.broadcasting.common.mapper.ContentMapper;
import com.mintpot.broadcasting.common.response.ContentResponse;
import com.mintpot.broadcasting.common.response.ResourceResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContentMapperImpl implements ContentMapper {

  private final ModelMapper mapper;

  @Override
  public ContentResponse toDto(Content content) {
    return mapper.map(content, ContentResponse.class);
  }

    @Override
    public ResourceResponse toResources(Content content) {
        return ResourceResponse.builder()
            .resourcePath(content.getUri())
            .fileType(content.getType())
            .fileSize(content.getSize())
            .build();
    }
}
