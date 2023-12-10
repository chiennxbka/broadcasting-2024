package com.mintpot.broadcasting.service.device;

import com.mintpot.broadcasting.common.entities.Device;
import com.mintpot.broadcasting.common.enums.DeviceStatus;
import com.mintpot.broadcasting.common.enums.ErrorCode;
import com.mintpot.broadcasting.common.enums.SearchOperation;
import com.mintpot.broadcasting.common.exception.BusinessException;
import com.mintpot.broadcasting.common.mapper.DeviceMapper;
import com.mintpot.broadcasting.common.model.ClientState;
import com.mintpot.broadcasting.common.model.Imei;
import com.mintpot.broadcasting.common.model.StateDetailClient;
import com.mintpot.broadcasting.common.request.DeviceRegis;
import com.mintpot.broadcasting.common.request.DeviceReq;
import com.mintpot.broadcasting.common.response.DeviceResponse;
import com.mintpot.broadcasting.repository.device.DeviceRepository;
import com.mintpot.broadcasting.repository.specs.DeviceSpecification;
import com.mintpot.broadcasting.repository.specs.SearchCriteria;
import com.mintpot.broadcasting.repository.user.UserRepository;
import com.mintpot.broadcasting.service.facade.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final ModelMapper mapper;

    private final AuthenticationFacade facade;

    private final DeviceMapper deviceMapper;

    private final DeviceRepository repository;

    private final UserRepository userRepository;

    private final GeometryFactory geometryFactory;

    @Value("${server.mqtt.api}")
    private String baseUrl;

    @Value("${server.mqtt.password}")
    private String password;

    @Override
    public Page<DeviceResponse> getCollections(Pageable pageable, String name, String imei) {
        var specification = new DeviceSpecification();
        if (!StringUtils.isEmpty(name))
            specification.add(new SearchCriteria("name", name, SearchOperation.MATCH));
        if (!StringUtils.isEmpty(imei))
            specification.add(new SearchCriteria("imei", imei, SearchOperation.EQUAL));
        Page<Device> devices = repository.findAll(specification, pageable);
        if (devices.isEmpty())
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        Page<DeviceResponse> response = new PageImpl<>
            (devices.getContent().stream().map(deviceMapper::toDto).collect(Collectors.toList()),
                pageable, devices.getTotalElements());
        this.setState(response.getContent());
        return response;
    }

    private void setState(List<DeviceResponse> devices) {
        byte[] encodedAuth = Base64.encodeBase64(password.getBytes(StandardCharsets.UTF_8));
        Map<String, Integer> params = new HashMap<>();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + new String(encodedAuth));
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<ClientState> response = restTemplate.exchange(baseUrl, HttpMethod.GET, entity, ClientState.class, params);
        List<StateDetailClient> state = Objects.requireNonNull(response.getBody()).getData();
        for (StateDetailClient stateDetailClient : state) {
            for (DeviceResponse dev : devices) {
                if (dev.getImei().equals(stateDetailClient.getClientId())) {
                    dev.setState(DeviceStatus.ONLINE);
                }
            }
        }
    }

    @Override
    public Device findByImei(String imei) {
        return repository.findByImei(imei);
    }

    @Override
    public DeviceResponse fetchById(Long id) {
        var device = repository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.OBJECT_NOTFOUND));
        return deviceMapper.toDto(device);
    }

    @Override
    public List<Imei> loadImei() {
        return repository.loadImei();
    }

    @Override
    public void persist(Device device) {
        repository.save(device);
    }

    @Override
    public Device register(DeviceRegis regis) {
        if (!userRepository.existsByUsername(regis.getUsername()))
            throw new BusinessException(ErrorCode.USER_NOTFOUND);
        var user = userRepository.findByUsername(regis.getUsername()).orElseThrow();
        var device = Device.builder()
            .name(regis.getDeviceName())
            .imei(regis.getImei())
            .organization(user.getOrganization())
            .active(true)
            .build();
        return repository.save(device);
    }

    @Override
    @Modifying
    public Long insert(DeviceReq form) {
        var device = mapper.map(form, Device.class);
        var organization = facade.getCurrentUser().getOrganization();
        device.setOrganization(organization);
        device.setActive(true);
        if (form.getLongitude() != null && form.getLatitude() != null)
            device.setLocation(geometryFactory.createPoint(new Coordinate(form.getLongitude(), form.getLatitude())));
        return repository.save(device).getId();
    }

    @Override
    @Modifying
    public void update(DeviceReq form) {
        if (repository.existsById(form.getId())) {
            var device = repository.getOne(form.getId());
            device.setName(form.getName());
            device.setImei(form.getImei());
            device.setDescription(form.getDescription());
            device.setLocation(geometryFactory.createPoint(new Coordinate(form.getLongitude(), form.getLatitude())));
            repository.save(device);
        }
    }

    @Override
    @ReadOnlyProperty
    public Device findById(Long id) {
        return repository.findById(id).orElseThrow(()
            -> new BusinessException(ErrorCode.OBJECT_NOTFOUND));
    }

    @Override
    @Modifying
    public void remove(Long id) {
        if (repository.existsById(id))
            repository.deleteById(id);
    }
}
