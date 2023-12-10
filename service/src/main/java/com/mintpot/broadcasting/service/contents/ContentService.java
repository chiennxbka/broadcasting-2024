package com.mintpot.broadcasting.service.contents;


import com.mintpot.broadcasting.common.entities.Content;
import com.mintpot.broadcasting.common.enums.MediaType;
import com.mintpot.broadcasting.common.request.ContentReq;
import com.mintpot.broadcasting.common.response.ContentResponse;
import com.mintpot.broadcasting.service.generic.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContentService extends Service<Content, ContentReq> {

  Page<ContentResponse> getCollections(Pageable pageable, String name, MediaType type);

  ContentResponse fetchById(Long id);

}
