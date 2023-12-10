package com.mintpot.broadcasting.common.mapper;

import com.mintpot.broadcasting.common.entities.Campaign;
import com.mintpot.broadcasting.common.response.CampaignResponse;
import org.mapstruct.Mapper;

@Mapper
public interface CampaignMapper {

  default CampaignResponse toDto(Campaign campaign) {
    return null;
  }
}
