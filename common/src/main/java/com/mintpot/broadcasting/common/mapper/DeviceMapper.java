package com.mintpot.broadcasting.common.mapper;

import com.mintpot.broadcasting.common.entities.Device;
import com.mintpot.broadcasting.common.response.DeviceResponse;
import org.mapstruct.Mapper;

@Mapper
public interface DeviceMapper {

  default DeviceResponse toDto(Device device) {
    return null;
  }
}
