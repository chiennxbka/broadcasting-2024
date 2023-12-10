package com.mintpot.broadcasting.common.mapper;

import com.mintpot.broadcasting.common.entities.User;
import com.mintpot.broadcasting.common.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

  default UserResponse toDto(User user) {
    return null;
  }
}
