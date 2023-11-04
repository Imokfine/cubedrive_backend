package com.imokfine.cubedrive.controller;

import com.imokfine.cubedrive.service.FileService;
import com.imokfine.cubedrive.utils.Result;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("file")
@CrossOrigin
public class FileController {

    @Resource
    private FileService fileService;

    // 上传文件
    @PostMapping("upload")
    public Result upload(MultipartFile file) throws IOException {
        Result result = fileService.upload(file);
        return result;
    }

    // 下载文件
    @GetMapping("download/{fileName}")
    public Result download(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        Result result = fileService.download(fileName,response);
        return result;
    }

    // 加载文件列表（分页模糊查询）
    @GetMapping("loadFileList")
    public Result loadFileList(String fileName,
                               Integer pageNum,
                               Integer pageSize,
                               Integer fileCategory){
        Result result = fileService.loadFileList(fileName, pageNum, pageSize, fileCategory);
        return result;
    }

    @GetMapping("showAll")
    public Result showAll(){
        Result result = fileService.showAll();
        return result;
    }
}
