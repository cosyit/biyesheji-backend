package com.edu.college.common.ret;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * Json 返回默认使用JsonData去进行返回。
 * 前后端分离项目，给前端进行提示的一些辅助功能，
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("unused")
public class Response {

    //返回结果
    private boolean ret;

    //返回状态码。
    private Integer code;

    //返回消息
    private String msg;

    //返回数据
    private Object data;

    //fail 的共同特征是ret 是false.
    public static Response fail(String msg) {
        //失败的时候，无返回，数据肯定没有，给个自定义消息即可。
        Response response = new Response();
        response.code = 500;
        response.ret = false;
        response.setMsg(msg);
        return response;
    }

    //自定义错误。 但是不应该有自定义错误。保留此，应付特殊情况。用的时候要验证，不要和系统和自定义的code值重复。
    public static Response fail(Integer errorCode, String message) {
        //失败的时候，无返回，数据肯定没有，给个自定义消息即可。
        Response response = new Response();
        response.setMsg(message);
        response.ret = false;
        response.setCode(errorCode);
        return response;
    }


    //success 共同的特征是ret 为true.

    //无消息数据的成功
    public static Response success() {
        Response response = new Response();
        response.ret = true;
        response.code = 200;
        return response;
    }

    //无消息的成功。
    public static Response success(Object object) {
        Response response = new Response();
        response.ret = true;
        response.data = object;
        response.code = 200;
        return response;
    }

    //三个数据都有的成功。 自定义消息的成功。
    public static Response success(Object data, String msg) {
        //成功的时候返回值为true.
        Response response = new Response();
        response.ret = true;
        response.setData(data);
        response.setMsg(msg);
        response.code = 200;
        return response;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("ret", ret);
        result.put("code", code);
        result.put("msg", msg);
        result.put("data", data);
        return result;
    }
}
