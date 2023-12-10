package com.mintpot.broadcasting.common.response;

import com.mintpot.broadcasting.common.enums.DeviceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class DeviceResponse {

    private Long id;

    private String name;

    private String imei;

    private String description;

    private double latitude;

    private double longitude;

    private DeviceStatus state;
}
