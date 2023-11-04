package com.imokfine.cubedrive.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    @TableId
    private Integer userId;

    private String username;

    private String email;

    private String password;

    private String nickname;

    private Date registerTime;

    private Integer isDeleted;

    private static final long serialVersionUID = 1L;
}