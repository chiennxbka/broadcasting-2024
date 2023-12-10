package com.mintpot.broadcasting.service.mapper;

import com.mintpot.broadcasting.common.entities.Device;
import com.mintpot.broadcasting.common.enums.DeviceStatus;
import com.mintpot.broadcasting.common.mapper.DeviceMapper;
import com.mintpot.broadcasting.common.response.DeviceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeviceMapperImpl implements DeviceMapper {
    @Override
    public DeviceResponse toDto(Device device) {
        return DeviceResponse.builder()
            .id(device.getId())
            .name(device.getName())
            .imei(device.getImei())
            .description(device.getDescription())
            .latitude(device.getLocation() != null ? device.getLocation().getX() : 0)
            .longitude(device.getLocation() != null ? device.getLocation().getY() : 0)
            .state(DeviceStatus.OFFLINE)
            .build();
    }
}
