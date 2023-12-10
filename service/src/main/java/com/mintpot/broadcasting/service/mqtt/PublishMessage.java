package com.mintpot.broadcasting.service.mqtt;//package com.nxc.broadcasting.service.mqtt;
//
//
//import org.eclipse.paho.client.mqttv3.MqttClient;
//
//import java.util.Set;
//
///**
// * @author SyNT
// * @CreatedDate Apr 25, 2018
// */
//public class PublishMessage implements Runnable {
//
//	private MqttUtils mqttUtils;
//
//	private MqttClient mqttClient;
//
//	private IDeviceMonitorRepository iDeviceRepo;
//
//	private DeviceMonitorDb device;
//
//	private Long newConfigId;
//
//	private String newUrlPath;
//
//	private Set<ResourceDto> temp;
//
//	/**
//	 * @param systemParameterUtils
//	 * @param env
//	 * @param iResourceRepository
//	 * @param mqttUtils
//	 * @param mqttClient
//	 * @param iDeviceRepo
//	 * @param iConfigurationRepo
//	 * @param device
//	 */
//	public PublishMessage(MqttUtils mqttUtils, MqttClient mqttClient, IDeviceMonitorRepository iDeviceRepo,
//                        DeviceMonitorDb device, Long newConfigId, String newUrlPath, Set<ResourceDto> temp) {
//		super();
//		this.mqttUtils = mqttUtils;
//		this.mqttClient = mqttClient;
//		this.iDeviceRepo = iDeviceRepo;
//		this.device = device;
//		this.newConfigId = newConfigId;
//		this.newUrlPath = newUrlPath;
//		this.temp = temp;
//	}
//
//	@Override
//	public void run() {
//		publishMsg(device);
//	}
//
//	private void publishMsg(DeviceMonitorDb device) {
//		MqttPublishData data = new MqttPublishData();
//		device.setConfigurationId(newConfigId);
//		device.setConfigurationUrl(newUrlPath.toString());
//		iDeviceRepo.merge(device);
//		data.setImei(device.getImei());
//		data.setResources(temp);
//		data.setClientId(mqttClient.getClientId());
//		mqttUtils.sendMsg(mqttClient, data, device.getImei(), false);
//	}
//}
