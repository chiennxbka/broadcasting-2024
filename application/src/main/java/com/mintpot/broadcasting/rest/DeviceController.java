package com.mintpot.broadcasting.rest;

import com.mintpot.broadcasting.common.entities.Device;
import com.mintpot.broadcasting.common.enums.ErrorCode;
import com.mintpot.broadcasting.common.exception.BusinessException;
import com.mintpot.broadcasting.common.request.DeviceReq;
import com.mintpot.broadcasting.common.response.DeviceResponse;
import com.mintpot.broadcasting.service.device.DeviceService;
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
@Api(tags = {"[Admin APIs] Device management"})
public class DeviceController extends AbstractController {

    private final DeviceService service;

    @GetMapping("/device")
    @ApiOperation(value = "Get device, pagination")
    public ResponseEntity<Page<DeviceResponse>> getCollections(
        @ApiParam(value = "Page number") @RequestParam(required = false, defaultValue = "0") int page,
        @ApiParam(value = "Page size") @RequestParam(required = false, defaultValue = "10") int offset,
        @ApiParam(value = "Name") @RequestParam(required = false) String name,
        @ApiParam(value = "Name") @RequestParam(required = false) String imei) {
        Pageable pageable = PageRequest.of(page, offset, Sort.by("name"));
        Page<DeviceResponse> devices = service.getCollections(pageable, name, imei);
        return ResponseEntity.ok(devices);
    }

    @PostMapping("/device")
    @ApiOperation(value = "Create a device")
    public ResponseEntity<Void> insert(@Valid @RequestBody DeviceReq form) throws IOException {
        Long id = service.insert(form);
        URI uri = URI.create(String.format("/api/v2/contents/%d", id));
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/device/{id}")
    @ApiOperation(value = "Update device by id")
    public ResponseEntity<Void> update(@PathVariable(value = "id") Long id,
                                    @Valid @RequestBody DeviceReq form) throws IOException {
        if (service.findById(id) == null)
            throw new BusinessException(ErrorCode.OBJECT_NOTFOUND);
        service.update(form);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/device/{id}")
    @ApiOperation(value = "Get device by id")
    public ResponseEntity<Device> findById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @DeleteMapping(value = "/device/{id}")
    @ApiOperation(value = "Delete a device")
    public ResponseEntity<Void> remove(@PathVariable(name = "id") Long id) {
        service.remove(id);
        return ResponseEntity.noContent().build();
    }
}
