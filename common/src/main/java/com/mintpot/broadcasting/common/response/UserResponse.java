package com.mintpot.broadcasting.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mintpot.broadcasting.common.enums.Gender;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
public class UserResponse {
    private long id;

    private Gender gender;

    private String phone;

    private String role;

    private Date birthday;

    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Object> data;
}
