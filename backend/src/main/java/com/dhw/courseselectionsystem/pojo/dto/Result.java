package com.dhw.courseselectionsystem.pojo.dto;

import lombok.Data;

/**
 * 全局统一返回结果类（带泛型，推荐）
 * code：1成功  0失败
 */
@Data
public class Result<T> {

    private Integer code;
    private String msg;
    private T data;

    // 私有化构造，禁止外部 new
    private Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // ====================== 成功返回 ======================
    /**
     * 成功 - 只返回状态，不返回数据
     */
    public static <T> Result<T> success() {
        return new Result<>(1, "success", null);
    }

    /**
     * 成功 - 返回数据（最常用）
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(1, "success", data);
    }

    // ====================== 失败返回 ======================
    /**
     * 失败 - 只返回错误信息
     */
    public static <T> Result<T> error(String msg) {
        return new Result<>(0, msg, null);
    }
}