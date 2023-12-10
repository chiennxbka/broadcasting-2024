package com.mintpot.broadcasting.rest;

import com.mintpot.broadcasting.common.model.DeviceChecking;
import com.mintpot.broadcasting.common.request.DeviceRegis;
import com.mintpot.broadcasting.service.device.DeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Api(tags = {"[Device] Public resource"})
public class PublicController extends AbstractController {

    private final DeviceService service;

    @Value(value = "${server.mqtt.uri.outbound}")
    private String serverUri;

    @GetMapping(value = "/public/device/imei")
    @ApiOperation(value = "Check device is exist")
    public ResponseEntity<Map<String, Object>> checkDeviceExist(@RequestParam(name = "imei") String imei) {
        var device = service.findByImei(imei);
        if (device == null)
            return ResponseEntity.ok(Map.of("exist", false));
        return ResponseEntity.ok(Map.of("exist", true, "data", DeviceChecking.builder()
            .imei(device.getImei()).device(device.getName()).serverUri(serverUri).build()));
    }

    @PostMapping(value = "/public/device/register")
    @ApiOperation(value = "Register device")
    public ResponseEntity<DeviceChecking> register(@RequestBody DeviceRegis regis) {
        var device = service.register(regis);
        return ResponseEntity.ok(DeviceChecking.builder()
            .imei(device.getImei()).device(device.getName()).serverUri(serverUri).build());
    }
}
