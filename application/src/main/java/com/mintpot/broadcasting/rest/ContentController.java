package com.mintpot.broadcasting.rest;

import com.mintpot.broadcasting.common.entities.Content;
import com.mintpot.broadcasting.common.enums.ErrorCode;
import com.mintpot.broadcasting.common.enums.MediaType;
import com.mintpot.broadcasting.common.exception.BusinessException;
import com.mintpot.broadcasting.common.request.ContentReq;
import com.mintpot.broadcasting.common.response.ContentResponse;
import com.mintpot.broadcasting.service.contents.ContentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@Api(tags = {"[Admin APIs] Content management"})
public class ContentController extends AbstractController {

    private final ContentService service;

    @GetMapping("/content")
    @ApiOperation(value = "Get content, pagination")
    public ResponseEntity<Page<ContentResponse>> getCollections(
        @ApiParam(value = "Page number") @RequestParam(required = false, defaultValue = "0") int page,
        @ApiParam(value = "Page size") @RequestParam(required = false, defaultValue = "10") int offset,
        @ApiParam(value = "Name") @RequestParam(required = false) String name,
        @ApiParam(value = "Name") @RequestParam(required = false) MediaType type) {
        Pageable pageable = PageRequest.of(page, offset, Sort.by("name"));
        Page<ContentResponse> campaigns = service.getCollections(pageable, name, type);
        return ResponseEntity.ok(campaigns);
    }

    @PostMapping("/content")
    @ApiOperation(value = "Create a content")
    public ResponseEntity<Void> insert(@ModelAttribute ContentReq form) {
        Long id = service.insert(form);
        URI uri = URI.create(String.format("/api/v2/contents/%d", id));
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/content/{id}")
    @ApiOperation(value = "Update content by id")
    public ResponseEntity<Void> update(@PathVariable(value = "id") Long id, ContentReq form) {
        if (service.findById(id) == null)
            throw new BusinessException(ErrorCode.OBJECT_NOTFOUND);
        form.setId(id);
        service.update(form);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/content/{id}")
    @ApiOperation(value = "Get content by id")
    public ResponseEntity<Content> findById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @DeleteMapping(value = "/content/{id}")
    @ApiOperation(value = "Delete a content")
    public ResponseEntity<Void> remove(@PathVariable(name = "id") Long id) {
        service.remove(id);
        return ResponseEntity.noContent().build();
    }
}
