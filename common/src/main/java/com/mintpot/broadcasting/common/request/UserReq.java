package com.mintpot.broadcasting.common.request;

import com.mintpot.broadcasting.common.enums.Gender;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class UserReq implements Serializable {

    private @Setter Long id;

    private String name;

    private Gender gender;

    private String password;

    private String phone;

    private String email;

    private String username;

    private Long roleId;

    private MultipartFile avatar;

    private Date birthday;

    private String organizationName;
}
