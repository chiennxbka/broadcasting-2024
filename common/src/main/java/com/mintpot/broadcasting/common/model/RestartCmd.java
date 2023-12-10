package com.mintpot.broadcasting.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestartCmd {
	private String imei;
	private String clientId;
	private String cmd;
}
