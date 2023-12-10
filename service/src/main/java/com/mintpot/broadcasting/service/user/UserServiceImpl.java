package com.mintpot.broadcasting.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mintpot.broadcasting.common.entities.Organization;
import com.mintpot.broadcasting.common.entities.Role;
import com.mintpot.broadcasting.common.entities.User;
import com.mintpot.broadcasting.common.enums.CategoryFile;
import com.mintpot.broadcasting.common.enums.ErrorCode;
import com.mintpot.broadcasting.common.enums.SearchOperation;
import com.mintpot.broadcasting.common.exception.BusinessException;
import com.mintpot.broadcasting.common.mapper.UserMapper;
import com.mintpot.broadcasting.common.request.StatusRequest;
import com.mintpot.broadcasting.common.request.UserReq;
import com.mintpot.broadcasting.common.response.UserResponse;
import com.mintpot.broadcasting.common.utils.Utils;
import com.mintpot.broadcasting.repository.organization.OrganizationRepository;
import com.mintpot.broadcasting.repository.role.RoleRepository;
import com.mintpot.broadcasting.repository.specs.SearchCriteria;
import com.mintpot.broadcasting.repository.specs.UserSpecification;
import com.mintpot.broadcasting.repository.user.UserRepository;
import com.mintpot.broadcasting.service.facade.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ModelMapper mapper;

    private final ObjectMapper objectMapper;

    private final UserMapper userMapper;

    private final PasswordEncoder bCryptPasswordEncoder;

    private final AuthenticationFacade facade;

    private final UserRepository repository;

    private final RoleRepository roleRepository;

    private final OrganizationRepository organizationRepository;

    @Override
    public User findByUsername(String username) {
        return repository.findByUsername(username).orElse(null);
    }

    @Override
    @ReadOnlyProperty
    public List<UserResponse> getCollections(String fullName) {
        UserSpecification specification = new UserSpecification();
        if (!StringUtils.isEmpty(fullName))
            specification.add(new SearchCriteria("name", fullName, SearchOperation.MATCH));
        List<User> users = repository.findAll(specification);
        return users.stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Long insert(UserReq request) {
        this.checkDuplicate(request);
        User user = mapper.map(request, User.class);
        if (!StringUtils.isEmpty(request.getPassword()))
            user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        if (request.getAvatar() != null && !request.getAvatar().isEmpty()) {
            String path = Utils.uploadLargeFileToLocalServer(request.getAvatar(), CategoryFile.IMAGE, "user");
            user.setAvatar(path);
        }
        if (request.getRoleId() != null) {
            Role role = roleRepository.getOne(request.getRoleId());
            user.setRole(role);
        }
        var organization = Organization.builder()
            .name(request.getOrganizationName())
            .code(Utils.generateRandomString(6))
            .build();
        organizationRepository.save(organization);
        user.setOrganization(organization);
        user.setActive(true);
        repository.save(user);
        return user.getId();
    }

    @Override
    @Transactional
    public void update(UserReq request) {
        if (!repository.existsById(request.getId()))
            throw new BusinessException(ErrorCode.OBJECT_NOTFOUND);
        User entity = this.findById(request.getId());
        var organization = facade.getCurrentUser().getOrganization();
        if (!organization.getName().equals(request.getOrganizationName())) {
            organization.setName(request.getOrganizationName());
            organizationRepository.save(organization);
        }
        entity.setName(request.getName());
        entity.setGender(request.getGender());
        entity.setBirthday(request.getBirthday());
        if (StringUtils.isEmpty(request.getPassword())) {
            entity.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        }
        if (StringUtils.isEmpty(request.getEmail()) &&
            !request.getEmail().equals(entity.getEmail()) &&
            repository.findByEmail(request.getEmail()).isPresent()) {
            throw new DataIntegrityViolationException("User with email " + request.getEmail() + " already exists");
        } else {
            entity.setEmail(request.getEmail());
        }
        if (StringUtils.isEmpty(request.getPhone()) &&
            !request.getPhone().equals(entity.getPhone()) &&
            repository.findByPhone(request.getPhone()).isPresent()) {
            throw new DataIntegrityViolationException("User with phone number " + request.getPhone() + " already exists");
        } else {
            entity.setPhone(request.getPhone());
        }
        if (request.getAvatar() != null && !request.getAvatar().isEmpty()) {
            String path = Utils.uploadLargeFileToLocalServer(request.getAvatar(), CategoryFile.IMAGE, "user");
            entity.setAvatar(path);
        }
        if (request.getRoleId() != null && entity.getRole() != null
            && !request.getRoleId().equals(entity.getRole().getId())) {
            Role role = roleRepository.getOne(request.getRoleId());
            entity.setRole(role);
        }
        repository.saveAndFlush(entity);
    }

    @Override
    @ReadOnlyProperty
    public List<UserResponse> fetchAll() {
        List<User> users = repository.findAll();
        return users.stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @ReadOnlyProperty
    public UserResponse fetchById(Long id) {
        var user = repository.findById(id).orElseThrow(()
            -> new BusinessException(ErrorCode.OBJECT_NOTFOUND));
        return userMapper.toDto(user);
    }

    @Override
    public UserResponse getMe() throws JsonProcessingException {
        var user = facade.getCurrentUser();
        var userInfo =  userMapper.toDto(user);
        String jsonSetting = Utils.readFile("/opt/broadcasting/user-setting.json");
        var data = objectMapper.readValue(jsonSetting, Map.class);
        data.put("username", user.getUsername());
        data.put("displayName", user.getName());
        data.put("photoURL", user.getAvatar());
        data.put("email", user.getEmail());
        userInfo.setData(data);
        return userInfo;
    }

    @Override
    public List<User> getSummaries() {
        long uid = facade.getCurrentUserId();
        return repository.findAll().stream().filter(user -> user.getId() != uid).collect(Collectors.toList());
    }

    @Override
    public UserResponse updateStatus(StatusRequest request) {
        var user = facade.getCurrentUser();
        user.setStatus(request.getStatus());
        repository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public void remove(Long id) {
        if (repository.existsById(id))
            repository.deleteById(id);
    }

    private void checkDuplicate(UserReq request) {
        if (!StringUtils.isEmpty(request.getEmail()) && repository.findByEmail(request.getEmail()).isPresent()) {
            throw new DataIntegrityViolationException("User with email " + request.getEmail() + " already exists");
        }
        if (!StringUtils.isEmpty(request.getPhone()) && repository.findByPhone(request.getPhone()).isPresent()) {
            throw new DataIntegrityViolationException("User with phone number " + request.getPhone() + " already exists");
        }
        if (repository.findByUsername(request.getUsername()).isPresent()) {
            throw new DataIntegrityViolationException("User with username " + request.getUsername() + " already exists");
        }
    }
}
