package com.mintpot.broadcasting.common.request;


import com.mintpot.broadcasting.common.enums.Priority;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
public class CampaignReq {
    private @Setter Long id;

    private Long[] deviceIds;

    private Long[] contentIds;

    @NotEmpty
    @Size(min = 1, max = 255)
    private String name;

    @NotNull
    private Date startTime;

    @NotNull
    private Date endTime;

    private Priority priority;

    private boolean repeated;
}
