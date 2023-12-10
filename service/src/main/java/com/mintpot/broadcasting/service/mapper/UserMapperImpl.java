package com.mintpot.broadcasting.service.mapper;

import com.mintpot.broadcasting.common.entities.User;
import com.mintpot.broadcasting.common.mapper.UserMapper;
import com.mintpot.broadcasting.common.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {

  private final ModelMapper mapper;

  @Override
  public UserResponse toDto(User user) {
    return mapper.map(user, UserResponse.class);
  }
}
