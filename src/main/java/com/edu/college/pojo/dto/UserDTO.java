package com.edu.college.pojo.dto;

import com.edu.college.common.util.md5.MD5Util;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String name;

    private Short gender;

    private Short age;

    private String newTelephone;

    private String password;

    public String getPassword() {
        return MD5Util.encrypt(this.password);
    }
}
