package com.mintpot.broadcasting.service.contents;

import com.mintpot.broadcasting.common.entities.Content;
import com.mintpot.broadcasting.common.enums.ErrorCode;
import com.mintpot.broadcasting.common.enums.MediaType;
import com.mintpot.broadcasting.common.enums.SearchOperation;
import com.mintpot.broadcasting.common.exception.BusinessException;
import com.mintpot.broadcasting.common.mapper.ContentMapper;
import com.mintpot.broadcasting.common.request.ContentReq;
import com.mintpot.broadcasting.common.response.ContentResponse;
import com.mintpot.broadcasting.common.utils.Utils;
import com.mintpot.broadcasting.repository.content.ContentRepository;
import com.mintpot.broadcasting.repository.specs.ContentSpecification;
import com.mintpot.broadcasting.repository.specs.SearchCriteria;
import com.mintpot.broadcasting.service.facade.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {

    private final ModelMapper mapper;

    private final AuthenticationFacade facade;

    private final ContentMapper contentMapper;

    private final ContentRepository repository;

    @Override
    public Page<ContentResponse> getCollections(Pageable pageable, String name, MediaType type) {
        ContentSpecification specification = new ContentSpecification();
        if (!StringUtils.isEmpty(name))
            specification.add(new SearchCriteria("name", name, SearchOperation.MATCH));
        if (type != null)
            specification.add(new SearchCriteria("type", type, SearchOperation.EQUAL));
        Page<Content> contents = repository.findAll(specification, pageable);
        if (contents.isEmpty())
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        return new PageImpl<>(contents.getContent().stream().map(contentMapper::toDto).collect(Collectors.toList()),
            pageable, contents.getTotalElements());
    }

    @Override
    public ContentResponse fetchById(Long id) {
        Content content = repository.findById(id).orElseThrow(()
            -> new BusinessException(ErrorCode.OBJECT_NOTFOUND));
        return contentMapper.toDto(content);
    }

    @Override
    public Long insert(ContentReq form) {
        Content content = mapper.map(form, Content.class);
        var organization = facade.getCurrentUser().getOrganization();
        content.setOrganization(organization);
        content.setActive(true);
        if (form.getResource() != null && !form.getResource().isEmpty()) {
            String pathResource = Utils.uploadLargeFileToLocalServer(form.getResource(), form.getType(), "content");
            content.setUri(pathResource);
            content.setSize(form.getResource().getSize());
        }
        return repository.save(content).getId();
    }

    @Override
    public void update(ContentReq form){
        if (repository.existsById(form.getId())) {
            Content content = repository.getOne(form.getId());
            content.setName(form.getName());
            if (form.getResource() != null && !form.getResource().isEmpty()) {
                String pathResource = Utils.uploadLargeFileToLocalServer(form.getResource(), form.getType(), "content");
                content.setUri(pathResource);
                content.setSize(form.getResource().getSize());
            }
            repository.save(content);
        }
    }

    @Override
    public void remove(Long id) {
        if (repository.existsById(id))
            repository.deleteById(id);
    }
}
