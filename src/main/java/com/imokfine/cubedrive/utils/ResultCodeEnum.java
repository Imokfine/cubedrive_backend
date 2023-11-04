package com.imokfine.cubedrive.utils;

import lombok.Getter;

/**
 * 统一返回结果状态信息类
 *
 */
@Getter
public enum ResultCodeEnum {

    SUCCESS(200,"success"),
    EMAIL_ERROR(501,"emailError"),
    PASSWORD_ERROR(503,"passwordError"),
    NOTLOGIN(504,"notLogin"),
    EMAIL_USED(505,"emailUsed"),
    REGISTER_ERROR(506,"registerError"),
    FILE_ERROR(507,"fileError");

    // 这里的状态码通常根据公司自己的定义来
    private Integer code;
    private String message;

    // 枚举类构造器默认为 private
    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}

