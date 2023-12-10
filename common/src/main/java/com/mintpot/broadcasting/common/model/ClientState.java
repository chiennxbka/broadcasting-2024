package com.mintpot.broadcasting.common.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClientState {
    private String code;
    private List<StateDetailClient> data;
}
