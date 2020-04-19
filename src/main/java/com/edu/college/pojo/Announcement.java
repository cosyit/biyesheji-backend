package com.edu.college.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Announcement {
    private Integer id;

    private String title;

    private Integer fromUserId;

    private Integer toUserId;

    private String content;
}