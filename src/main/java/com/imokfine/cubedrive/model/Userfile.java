package com.imokfine.cubedrive.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName userfile
 */
@TableName(value ="userfile")
@Data
public class Userfile implements Serializable {
    @TableId
    private Integer userFileid;

    private String extendName;

    private Integer userId;

    private Integer fileId;

    private String fileName;

    private String filePath;

    private Integer isDir;

    private String uploadTime;

    private Integer isDeleted;

    private static final long serialVersionUID = 1L;
}