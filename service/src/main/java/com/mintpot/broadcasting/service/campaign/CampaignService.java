package com.mintpot.broadcasting.service.campaign;


import com.mintpot.broadcasting.common.entities.Campaign;
import com.mintpot.broadcasting.common.request.CampaignReq;
import com.mintpot.broadcasting.common.response.CampaignResponse;
import com.mintpot.broadcasting.service.generic.Service;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CampaignService extends Service<Campaign, CampaignReq> {

  @ReadOnlyProperty
  Page<CampaignResponse> getCollections(Pageable pageable, String name);

  CampaignResponse fetchById(Long id);

  List<CampaignResponse> fetchAll();

    void runable(Long id);
}
