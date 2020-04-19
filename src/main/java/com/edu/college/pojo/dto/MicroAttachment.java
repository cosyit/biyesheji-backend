package com.edu.college.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MicroAttachment {

    private Integer id;

    private String fileName;

    private String fileType;

    private String filePath;

    private String thumbPath;

    private String creatorName;

    private Long fileSize;

    private String remark;

    private Date createTime;

    private String operator;

    private Date operatorTime;

    private String operatorIp;
}