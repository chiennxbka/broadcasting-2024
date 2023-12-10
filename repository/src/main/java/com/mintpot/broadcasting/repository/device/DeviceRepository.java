package com.mintpot.broadcasting.repository.device;

import com.mintpot.broadcasting.common.entities.Device;
import com.mintpot.broadcasting.common.model.Imei;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long>, JpaSpecificationExecutor<Device> {
    Set<Device> findDeviceByIdIn(List<Long> ids);

    Device findByImei(String imei);

    @Query(value = "SELECT imei FROM Device")
    List<Imei> loadImei();
}
