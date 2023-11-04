package com.imokfine.cubedrive.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName file
 */
@TableName(value ="file")
@Data
public class File implements Serializable {
    @TableId
    private Integer fileId;

    private Integer userId;

    private String fileMd5;

    private Integer filePid;

    private String fileName;

    private Long fileSize;

    private String fileUrl;

    private Date createTime;

    private Integer folderType;

    private Integer fileCategory;

    private Integer fileType;

    private Integer status;

    private Date recoveryTime;

    private Integer delFlag;

    private Integer isDeleted;

    private static final long serialVersionUID = 1L;
}