package com.mintpot.broadcasting.rest;

import com.mintpot.broadcasting.common.request.CampaignReq;
import com.mintpot.broadcasting.common.response.CampaignResponse;
import com.mintpot.broadcasting.service.campaign.CampaignService;
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

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;


@RestController
@RequiredArgsConstructor
@Api(tags = {"[Admin APIs] Campaign management"})
public class CampaignController extends AbstractController {

    private final CampaignService service;

    @GetMapping("/campaign")
    @ApiOperation(value = "Get campaign, pagination")
    public ResponseEntity<Page<CampaignResponse>> getCollections(
        @ApiParam(value = "Page number") @RequestParam(required = false, defaultValue = "0") int page,
        @ApiParam(value = "Page size") @RequestParam(required = false, defaultValue = "10") int offset,
        @ApiParam(value = "Name") @RequestParam(required = false) String name) {
        Pageable pageable = PageRequest.of(page, offset, Sort.by("name"));
        Page<CampaignResponse> campaigns = service.getCollections(pageable, name);
        return ResponseEntity.ok(campaigns);
    }

    @PostMapping("/campaign")
    @ApiOperation(value = "Create a campaign")
    public ResponseEntity<Void> insert(@Valid @RequestBody CampaignReq form) throws IOException {
        Long id = service.insert(form);
        URI uri = URI.create(String.format("/api/v2/campaign/%d", id));
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/campaign/{id}")
    @ApiOperation(value = "Update campaign by id")
    public ResponseEntity<Void> update(@PathVariable(value = "id") Long id,
                                       @Valid @RequestBody CampaignReq form) {
        form.setId(id);
        service.update(form);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/campaign/runable/{id}")
    @ApiOperation(value = "Update campaign by id")
    public ResponseEntity<Void> runable(@PathVariable(value = "id") Long id) {
        service.runable(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/campaign/{id}")
    @ApiOperation(value = "Get campaign by id")
    public ResponseEntity<CampaignResponse> findById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(service.fetchById(id));
    }

    @DeleteMapping(value = "/campaign/{id}")
    @ApiOperation(value = "Delete campaign by id")
    public ResponseEntity<Void> remove(@PathVariable(name = "id") Long id) {
        service.remove(id);
        return ResponseEntity.ok().build();
    }
}
