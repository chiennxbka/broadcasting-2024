package com.mintpot.broadcasting.service.facade;

import com.mintpot.broadcasting.common.entities.User;
public interface AuthenticationFacade {

    Long getCurrentUserId();

    User getCurrentUser();
}
