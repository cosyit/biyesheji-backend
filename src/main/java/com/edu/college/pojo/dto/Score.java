package com.edu.college.pojo.dto;

import com.edu.college.common.util.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Score {
    @ExcelColumn("成绩")
    private double score;
}
