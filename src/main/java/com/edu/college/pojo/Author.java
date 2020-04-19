package com.edu.college.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    private Integer id;

    private Integer seq;

    private Integer achievementId;

    private String authorName;

    private Short gender;

    private String department;

    private Integer contribution;
}