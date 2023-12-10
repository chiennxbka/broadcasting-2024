package com.mintpot.broadcasting.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StateDetailClient {
    @JsonProperty("clientid")
    private String clientId;
    @JsonProperty("username")
    private String username;
    @JsonProperty("ip_address")
    private String ipAddress;
    @JsonProperty("port")
    private int port;
    @JsonProperty("clean_sess")
    private boolean cleanSess;
    @JsonProperty("proto_ver")
    private int protoVer;
    @JsonProperty("keepalive")
    private int keepalive;
    @JsonProperty("connected")
    private boolean connected;
    @JsonProperty("connected_at")
    private String connectedAt;
}
