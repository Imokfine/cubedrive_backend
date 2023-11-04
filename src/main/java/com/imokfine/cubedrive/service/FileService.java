package com.imokfine.cubedrive.service;

import com.imokfine.cubedrive.model.File;
import com.baomidou.mybatisplus.extension.service.IService;
import com.imokfine.cubedrive.utils.Result;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
* @author ponponpon
* @description 针对表【file】的数据库操作Service
* @createDate 2023-10-28 22:33:22
*/
public interface FileService extends IService<File> {

    Result upload(MultipartFile file) throws IOException;

    Result download(String fileName, HttpServletResponse response) throws IOException;

    Result loadFileList(String fileName, Integer pageNum, Integer pageSize, Integer fileCategory);

    Result showAll();
}
