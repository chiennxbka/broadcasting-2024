package com.mintpot.broadcasting.service.mapper;

import com.mintpot.broadcasting.common.entities.Campaign;
import com.mintpot.broadcasting.common.mapper.CampaignMapper;
import com.mintpot.broadcasting.common.response.CampaignResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CampaignMapperImpl implements CampaignMapper {

  private final ModelMapper mapper;

  @Override
  public CampaignResponse toDto(Campaign campaign) {
    return mapper.map(campaign, CampaignResponse.class);
  }
}
