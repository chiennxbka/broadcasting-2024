package com.mintpot.broadcasting.service.campaign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mintpot.broadcasting.common.entities.Campaign;
import com.mintpot.broadcasting.common.entities.Content;
import com.mintpot.broadcasting.common.entities.Device;
import com.mintpot.broadcasting.common.enums.ErrorCode;
import com.mintpot.broadcasting.common.enums.SearchOperation;
import com.mintpot.broadcasting.common.exception.BusinessException;
import com.mintpot.broadcasting.common.mapper.CampaignMapper;
import com.mintpot.broadcasting.common.mapper.ContentMapper;
import com.mintpot.broadcasting.common.model.MqttPublishData;
import com.mintpot.broadcasting.common.request.CampaignReq;
import com.mintpot.broadcasting.common.response.CampaignResponse;
import com.mintpot.broadcasting.repository.campaign.CampaignRepository;
import com.mintpot.broadcasting.repository.content.ContentRepository;
import com.mintpot.broadcasting.repository.device.DeviceRepository;
import com.mintpot.broadcasting.repository.specs.CampaignSpecification;
import com.mintpot.broadcasting.repository.specs.SearchCriteria;
import com.mintpot.broadcasting.service.facade.AuthenticationFacade;
import com.mintpot.broadcasting.service.mqtt.MqttUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CampaignServiceImpl implements CampaignService {

    private final ModelMapper mapper;

    private final AuthenticationFacade facade;

    private final ObjectMapper objectMapper;

    private final ContentMapper contentMapper;

    private final CampaignMapper campaignMapper;

    private final CampaignRepository repository;

    private final ContentRepository contentRepository;

    private final DeviceRepository deviceRepository;

    private final MqttClient mqttClient;

    @Value("${service.mqtt.publish.topic}")
    public String publishTopic;

    @Override
    public Page<CampaignResponse> getCollections(Pageable pageable, String name) {
        CampaignSpecification specification = new CampaignSpecification();
        if (!StringUtils.isEmpty(name))
            specification.add(new SearchCriteria("name", name, SearchOperation.MATCH));
        Page<Campaign> campaigns = repository.findAll(specification, pageable);
        if (campaigns.isEmpty())
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        return new PageImpl<>(campaigns.getContent().stream().map(campaignMapper::toDto).collect(Collectors.toList()),
            pageable, campaigns.getTotalElements());
    }

    @Override
    public Long insert(CampaignReq form) {
        if (!this.validatingDate(form.getStartTime(), form.getEndTime()))
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "duration_time_invalid");
        Campaign campaign = mapper.map(form, Campaign.class);
        var organization = facade.getCurrentUser().getOrganization();
        Set<Device> devices = deviceRepository.findDeviceByIdIn(Arrays.asList(form.getDeviceIds()));
        Set<Content> contents = contentRepository.findContentByIdIn(Arrays.asList(form.getContentIds()));
        campaign.setContents(contents);
        campaign.setActive(true);
        campaign.setDevices(devices);
        campaign.setOrganization(organization);
        return repository.save(campaign).getId();
    }

    @Override
    public void update(CampaignReq form) {
        if (repository.existsById(form.getId())) {
            if (!this.validatingDate(form.getStartTime(), form.getEndTime()))
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "duration_time_invalid");
            Campaign campaign = repository.getOne(form.getId());
            Set<Device> devices = deviceRepository.findDeviceByIdIn(Arrays.asList(form.getDeviceIds()));
            Set<Content> contents = contentRepository.findContentByIdIn(Arrays.asList(form.getContentIds()));
            campaign.setContents(contents);
            campaign.setDevices(devices);
            campaign.setName(form.getName());
            campaign.setStartTime(form.getStartTime());
            campaign.setEndTime(form.getEndTime());
            campaign.setPriority(form.getPriority());
            campaign.setRepeated(form.isRepeated());
            repository.save(campaign);
        }
    }

    @Override
    @ReadOnlyProperty
    public CampaignResponse fetchById(Long id) {
        Campaign campaign = repository.findById(id).orElseThrow(()
            -> new BusinessException(ErrorCode.OBJECT_NOTFOUND));
        return campaignMapper.toDto(campaign);
    }

    @Override
    @ReadOnlyProperty
    public List<CampaignResponse> fetchAll() {
        List<Campaign> campaigns = repository.findAll();
        return campaigns.stream().map(campaignMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void runable(Long id) {
        Campaign campaign = repository.findById(id).orElseThrow(()
            -> new BusinessException(ErrorCode.OBJECT_NOTFOUND));
        Set<Device> devices = campaign.getDevices();
        Set<Content> contents = campaign.getContents();
        if (!devices.isEmpty()) {
            devices.forEach(device -> {
                var mqttPublishData = MqttPublishData.builder()
                    .imei(device.getImei())
                    .resources(contents.stream().map(contentMapper::toResources).collect(Collectors.toSet()))
                    .build();
                MqttUtils.sendMsgToQueue(mqttClient, objectMapper, mqttPublishData, device.getImei(), publishTopic);
            });
        }
    }

    @Override
    public void remove(Long id) {
        if (repository.existsById(id))
            repository.deleteById(id);
    }

    private boolean validatingDate(Date startTime, Date endTime) {
        return startTime.before(endTime);
    }
}
