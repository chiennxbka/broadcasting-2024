package com.mintpot.broadcasting.common.response;

import com.mintpot.broadcasting.common.enums.Priority;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class CampaignResponse {

  private Long id;

  private String name;

  private Date startTime;

  private Date endTime;

  private Priority priority;

  private boolean repeated;
}
