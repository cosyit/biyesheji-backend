package com.edu.college.pojo.vo;

import com.edu.college.pojo.Attachment;
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
public class AchievementVO {
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

    private Date publishTime;

    private String publishScope;

    private Integer wordCount;

    private Boolean translation;

    private Boolean result;

    private String language;

    private String cnIssn;

    private String isbn;

    private Short status;

    private Date reviewTime;

    private String comment;
    private String reviewer;

    private List<Author> authors;

    private List<Attachment> attachments;
}
