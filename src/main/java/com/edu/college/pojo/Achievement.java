package com.edu.college.pojo;

import com.edu.college.pojo.dto.AchievementDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Achievement {
    private Integer id;

    private Integer userId;

    private String title;

    private String number;

    private String firstAuthor;

    private String department;

    private String college;

    private String subject;

    private String categories;

    private String publishType;

    private String publishArea;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date publishTime;

    private String publishScope;

    private Integer wordCount;

    private Boolean translation;

    private Boolean result;

    private String language;

    private String cnIssn;

    private String isbn;

    private Short status;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date reviewTime;

    private String comment;
    private String type;

    public static Achievement of(AchievementDTO dto) {
        return Achievement.builder()
                .title(dto.getTitle())
                .number(dto.getNumber())
                .firstAuthor(dto.getFirstAuthor())
                .department(dto.getDepartment())
                .college(dto.getCollege())
                .subject(dto.getSubject())
                .categories(dto.getCategories())
                .publishType(dto.getPublishType())
                .publishArea(dto.getPublishArea())
                .publishTime(dto.getPublishTime())
                .publishScope(dto.getPublishScope())
                .wordCount(dto.getWordCount())
                .translation(dto.getTranslation())
                .language(dto.getLanguage())
                .cnIssn(dto.getCnIssn())
                .isbn(dto.getIsbn())
                .type(dto.getType())
                .build();
    }
}