package com.mintpot.broadcasting.service.user;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.mintpot.broadcasting.common.entities.User;
import com.mintpot.broadcasting.common.request.StatusRequest;
import com.mintpot.broadcasting.common.request.UserReq;
import com.mintpot.broadcasting.common.response.UserResponse;
import com.mintpot.broadcasting.service.generic.Service;

import java.util.List;

/**
 * @author ChienNX
 * @CreatedDate Oct 9, 2017 2:14:42 PM
 */
public interface UserService extends Service<User, UserReq> {

    User findByUsername(String username);

    List<UserResponse> getCollections(String fullName);

    List<UserResponse> fetchAll();

    UserResponse fetchById(Long id);

    UserResponse getMe() throws JsonProcessingException;

    List<User> getSummaries();

    UserResponse updateStatus(StatusRequest request);
}
