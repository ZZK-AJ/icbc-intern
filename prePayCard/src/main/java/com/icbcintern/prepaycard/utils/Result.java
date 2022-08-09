package com.icbcintern.prepaycard.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private int code;  // 默认值 0 为成功，1 为失败
    private String msg;
    private Object data;

    public static Result ok() {
        return new Result(0, "success", null);
    }

    public static Result unOk() {
        return new Result(1, "fail", null);
    }

    public static Result setSuccessMsg(String msg, Object data) {
        return new Result(0, msg, data);
    }

    public static Result setFailMsg(String msg, Object data) {

        return new Result(1, msg, data);
    }
}
