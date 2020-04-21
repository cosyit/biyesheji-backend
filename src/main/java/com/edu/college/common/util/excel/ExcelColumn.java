package com.edu.college.common.util.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumn {
    /**
     * <p>放在类的属性上，表明这个字段和excel文件的列名对应关系</p>
     * <p>excel表格的物理第一行必须是列名</p>
     * <p>这个列下的第二行开始都是这个列的值</p>
     */
    String value();
}
