package com.mintpot.broadcasting.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mintpot.broadcasting.common.entities.Campaign;
import com.mintpot.broadcasting.common.enums.CategoryFile;
import com.mintpot.broadcasting.common.mapper.ContentMapper;
import com.mintpot.broadcasting.common.model.Imei;
import com.mintpot.broadcasting.common.model.MqttPublishData;
import com.mintpot.broadcasting.common.model.RestartCmd;
import com.mintpot.broadcasting.common.model.SubscribeMessage;
import com.mintpot.broadcasting.common.response.ResourceResponse;
import com.mintpot.broadcasting.service.device.DeviceService;
import com.mintpot.broadcasting.service.mqtt.MqttUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class MqttProcessing {

    private final ObjectMapper mapper;

    private final ContentMapper contentMapper;

    private final DeviceService deviceService;

    private final MqttClient mqttClient;

    private final ThreadPoolTaskExecutor executor;

    @Value("${service.mqtt.subscribe.topic}")
    public String subscribeTopic;

    @Value("${service.mqtt.publish.topic}")
    public String publishTopic;

    @Value("${service.mqtt.restart.topic}")
    public String restartTopic;

    @PostConstruct
    public void subscribeMessageToMqttServer() {
        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setAutomaticReconnect(true);
            options.setUserName(UUID.randomUUID().toString());
            mqttClient.connect(options);
            mqttClient.subscribe(subscribeTopic, 2);
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    log.warn("---- Client has been disconnected");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    log.info(String.format("---- Topic: %s", topic));
                    executor.execute(() -> {
                        String msg = new String(message.getPayload());
                        try {
                            var sm = mapper.readValue(msg, SubscribeMessage.class);
                            var imei = sm.getImei();
                            var mqttPublishData = this.getData(imei);
                            this.sendMsg(mqttPublishData, imei);
                        } catch (IOException e) {
                            log.error(e.getMessage());
                        }
                    });
                }

                private void sendMsg(Object object, String imei) {
                    MqttUtils.sendMsgToQueue(mqttClient, mapper, object, imei, publishTopic);
                }

                private MqttPublishData getData(String imei) {
                    var data = new MqttPublishData();
                    data.setImei(imei);
                    var device = deviceService.findByImei(imei);
                    if (device != null) {
                        /*get campaign current active*/
                        Set<Campaign> campaigns = device.getCampaigns();
                        Optional<Campaign> campaignActive = campaigns.stream().filter(campaign ->
                            campaign.getStartTime().compareTo(new Date()) < 0 && campaign.getEndTime().compareTo(new Date()) > 0).findFirst();
                        if (campaignActive.isPresent()) {
                            data.setResources(campaignActive.get().getContents().stream().map(contentMapper::toResources).collect(Collectors.toSet()));
                        } else {
                            /*Run demo campaign --> replace default campaign...*/
                            data.setResources(resourceDefault());
                        }
                    }
                    return data;
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    log.info("---- Server has been delivery complete with token");
                }
            });
        } catch (MqttException e) {
            log.error(String.format("---- Occur server error: %s", e.getMessage()));
        }
    }

//	@Scheduled(fixedRate = Constants.FIXRATE)
//	public void runableTasK() throws InterruptedException {
//		List<DeviceMonitorDb> deviceMonitors = iDeviceRepo.loadAll();
//		for (DeviceMonitorDb device : deviceMonitors) {
//			Date currentTime = new Date();
//			// lay ra campain can chay sau do cap nhat device voi campain can chay
//			ConfigurationDb configurationDb = iConfigurationRepo.loadConfigurationActiveOfDevice(device.getDeviceId(),
//					currentTime);
//			if (configurationDb != null) {
//				Long newConfigId = configurationDb.getConfigurationId();
//				long oldConfigId = device.getConfigurationId() == null ? 0 : device.getConfigurationId();
//				Set<ResourceDto> temp = new HashSet<ResourceDto>();
//				// lay ra tat ca resource boi current config
//				List<ResourceDb> listResourceDb = iResourceRepository.loadAllByConfigurationId(newConfigId);
//				StringBuilder newUrlPath = new StringBuilder();
//				String baseUrl = com.sbd.cms.common.utils.Constants.HTTP +
//            systemParameterUtils.getServerInfo() + env.getProperty("server.context-path") +
//            File.separator;
//				for (ResourceDb resourceDb : listResourceDb) {
//					String path = baseUrl.concat(resourceDb.getResourceUrl()).replaceAll("\\\\", "/");
//					newUrlPath.append(path).append(", ");
//					temp.add(new ResourceDto(path, resourceDb.getResourceType(), resourceDb.getSize(),
//							resourceDb.getTimeNavigate()));
//				}
//
//				List<String> listNewUrlPath = new ArrayList<>();
//				// add urlPath ti list String
//				if (!StringUtils.isEmpty(newUrlPath)) {
//					// add to list
//					listNewUrlPath = Arrays.stream(newUrlPath.toString().split(",")).collect(Collectors.toList());
//				}
//
//				String oldUrlPath = device.getConfigurationUrl();
//				List<String> listOldUrlPath = new ArrayList<>();
//				if (!StringUtils.isEmpty(oldUrlPath)) {
//					listOldUrlPath = Arrays.stream(oldUrlPath.toString().split(",")).collect(Collectors.toList());
//				}
//
//				boolean resultCompareContent = Utils.compareTwoListString(listOldUrlPath, listNewUrlPath);
//				if (!resultCompareContent || (oldConfigId != newConfigId)) {
//					executor.execute(new PublishMessage(mqttUtils, mqttClient, iDeviceRepo, device, newConfigId,
//							newUrlPath.toString(), temp));
//				}
//			}
//			Thread.sleep(1000);
//		}
//	}

    @Scheduled(cron = "${server.cron.restart.device}")
    public void restartDeviceEveryDay() {
        List<Imei> imeiList = deviceService.loadImei();
        for (Imei i : imeiList) {
            String imei = i.getImei();
            RestartCmd cmd = new RestartCmd(imei, imei, "restart");
            restartDevice(cmd, imei);
        }
    }

    private void restartDevice(Object object, String imei) {
        MqttUtils.sendMsgToQueue(mqttClient, mapper, object, imei, restartTopic);
    }

    private Set<ResourceResponse> resourceDefault() {
        return Collections.singleton(ResourceResponse.builder()
            .fileType(CategoryFile.VIDEO)
            .fileSize(1200000L)
            .resourcePath("https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_30mb.mp4")
            .build());
    }
}
