package com.edu.college.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private static final Long serialVersionUID = 1L;
    private Integer id;

    private String name;

    private Short gender;

    private Short age;

    private String telephone;

    private String password;
}