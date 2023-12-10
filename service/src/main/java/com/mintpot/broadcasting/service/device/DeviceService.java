package com.mintpot.broadcasting.service.device;


import com.mintpot.broadcasting.common.entities.Device;
import com.mintpot.broadcasting.common.model.Imei;
import com.mintpot.broadcasting.common.request.DeviceRegis;
import com.mintpot.broadcasting.common.request.DeviceReq;
import com.mintpot.broadcasting.common.response.DeviceResponse;
import com.mintpot.broadcasting.service.generic.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DeviceService extends Service<Device, DeviceReq> {

    Page<DeviceResponse> getCollections(Pageable pageable, String name, String imei);

    Device findByImei(String imei);

    DeviceResponse fetchById(Long id);

    List<Imei> loadImei();

    void persist(Device device);

    Device register(DeviceRegis regis);
}
