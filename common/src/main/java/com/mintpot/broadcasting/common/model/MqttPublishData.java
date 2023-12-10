package com.mintpot.broadcasting.common.model;

import com.mintpot.broadcasting.common.response.ResourceResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MqttPublishData implements Serializable {
    private String imei;

    private Set<ResourceResponse> resources;
}
