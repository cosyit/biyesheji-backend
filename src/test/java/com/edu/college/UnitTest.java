package com.edu.college;

import com.edu.college.common.util.md5.MD5Util;
import org.junit.jupiter.api.Test;

public class UnitTest {
    @Test
    public void test1(){
        System.out.println(MD5Util.encrypt("123"));
    }
}
