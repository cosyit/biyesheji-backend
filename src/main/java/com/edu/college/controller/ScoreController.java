package com.edu.college.controller;

import com.edu.college.common.ret.Response;
import com.edu.college.common.util.excel.ExcelUtil;
import com.edu.college.pojo.dto.Score;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("score")
public class ScoreController {
    @PostMapping("upload")
    public Response upload(@RequestParam(value = "file") MultipartFile multipartFile) {
        if (multipartFile.getContentType() == null) {
            return Response.fail("没有上传文件");
        }
        try (final ExcelUtil excelUtil = new ExcelUtil(multipartFile.getInputStream())) {
            final List<Score> scores = excelUtil.read(0, Score.class);
            final long lt60 = scores.stream().mapToDouble(Score::getScore).filter(value -> value < 60).count();
            final long gte60lt70 = scores.stream().mapToDouble(Score::getScore).filter(value -> value >= 60 && value < 70).count();
            final long gte70lt80 = scores.stream().mapToDouble(Score::getScore).filter(value -> value >= 70 && value < 80).count();
            final long gte80lt90 = scores.stream().mapToDouble(Score::getScore).filter(value -> value >= 80 && value < 90).count();
            final long gte90 = scores.stream().mapToDouble(Score::getScore).filter(value -> value >= 90).count();
            final Map<String, Long> pie = new LinkedHashMap<>(5);
            pie.put("60以下", lt60);
            pie.put("60-70", gte60lt70);
            pie.put("70-80", gte70lt80);
            pie.put("80-90", gte80lt90);
            pie.put("90-100", gte90);
            return Response.success(pie);
        } catch (IOException e) {
            throw new RuntimeException("文件读取异常");
        }
    }
}
