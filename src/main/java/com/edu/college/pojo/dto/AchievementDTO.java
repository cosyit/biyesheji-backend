package com.edu.college.pojo.dto;

import com.edu.college.pojo.Author;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AchievementDTO {
    /**
     * 项目名称
     */
    private String title;
    /**
     * 项目编号/获奖题目
     */
    private String number;
    /**
     * 第一作者/完成人
     */
    private String firstAuthor;
    /**
     * 所属单位
     */
    private String department;
    /**
     * 学校署名
     */
    private String college;
    /**
     * 一级学科
     */
    private String subject;
    /**
     * 学课门类
     */
    private String categories;
    /**
     * 刊物/著作类型/获奖类别
     */
    private String publishType;
    /**
     * 出版地/发证机关
     */
    private String publishArea;
    /**
     * 发出/出版/获奖日期
     */
    private Date publishTime;
    /**
     * 发布/出版范围/单位/获奖级别
     */
    private String publishScope;
    /**
     * 字数(万)/项目经费(万) 获奖人数
     */
    private Integer wordCount;
    /**
     * 是否译文
     */
    private Boolean translation;
    /**
     * 语种
     */
    private String language;
    /**
     * CN或ISSN号
     */
    private String cnIssn;
    /**
     * ISBN号
     */
    private String isbn;

    private List<Author> authors;

    private List<String> attachments;
}
