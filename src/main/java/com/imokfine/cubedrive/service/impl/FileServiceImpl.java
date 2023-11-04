package com.imokfine.cubedrive.service.impl;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imokfine.cubedrive.model.File;
import com.imokfine.cubedrive.service.FileService;
import com.imokfine.cubedrive.mapper.FileMapper;
import com.imokfine.cubedrive.utils.Result;
import com.imokfine.cubedrive.utils.ResultCodeEnum;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
* @author ponponpon
* @description 针对表【file】的数据库操作Service实现
* @createDate 2023-10-28 22:33:22
*/
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File>
    implements FileService{

    @Value("${server.address}")
    private String serverAddress;

    @Value("${server.port}")
    private String serverPort;

    @Resource
    private FileMapper fileMapper;

    private static final String PROJECT_PATH = System.getProperty("user.dir"); // 从系统变量中拿到当前项目路径
    private static final String FILE_PARENT_PATH = PROJECT_PATH + "\\files"; // 自定义文件存储目录

    /**
     * 设置文件分类 1:视频 2:音频 3:图片 4:文档 5:其他
     * @param fileName
     * @return
     */
    public int classifyFileCategory(String fileName) {
        String[] videoExtensions = { "mp4", "avi", "mkv", "mov", "flv" };
        String[] audioExtensions = { "mp3", "wav", "ogg", "aac" };
        String[] imageExtensions = { "jpg", "jpeg", "png", "gif" };
        String[] documentExtensions = { "pdf", "doc", "docx", "xls", "xlsx", "txt" };

        String extName = FileUtil.extName(fileName);

        if (Arrays.asList(videoExtensions).contains(extName)) {
            return 1; // 视频
        } else if (Arrays.asList(audioExtensions).contains(extName)) {
            return 2; // 音频
        } else if (Arrays.asList(imageExtensions).contains(extName)) {
            return 3; // 图片
        } else if (Arrays.asList(documentExtensions).contains(extName)) {
            return 4; // 文档
        } else {
            return 5; // 其他
        }
    }

    /**
     * 上传文件
     * @param file
     * @return
     */
    @Override
    public Result upload(MultipartFile file) throws IOException {
        // 获取文件信息
        String fileName = file.getOriginalFilename(); // 获取文件名，例如 aaa.txt
        String mainName = FileUtil.mainName(fileName); // 获取文件主要名称，例如 aaa
        String extName = FileUtil.extName(fileName); // 获取文件后缀， 例如 txt

        // 转存的特定目录
        String filePath = FILE_PARENT_PATH + "\\" + fileName; // 文件路径
        java.io.File saveFile = new java.io.File(filePath);//要存的文件
        if(!saveFile.getParentFile().exists()){ // 如果文件路径父级目录存不存在，创建父级目录
            saveFile.getParentFile().mkdirs();
        }
        if(saveFile.exists()){ //如果上传的文件名已经存在，重命名当前的文件名称 (使用uuid生成随机数)
            fileName = mainName + "_" + UUID.randomUUID() + "." + extName;
            filePath = FILE_PARENT_PATH + "\\" + fileName;
            saveFile = new java.io.File(filePath);
        }
        file.transferTo(saveFile);
        String url = "http://" + serverAddress + ":" + serverPort + "/file/download/" + fileName; // download接口


        // 写入数据库
        File fileData = new File();
        fileData.setUserId(1); // TODO: 改成上传用户的id
        fileData.setFileName(fileName);
        fileData.setFileCategory(classifyFileCategory(fileName));  // 文件分类 1:视频 2:音频 3:图片 4:文档 5:其他
        fileData.setCreateTime(new Date());
        fileData.setFileSize(file.getSize()); // 单位为字节
        fileData.setFileUrl(url);
        fileMapper.insert(fileData);

        Map data = new HashMap();
        data.put("url", url);

        return Result.ok(data);
    }

    /**
     * 下载文件
     * @param fileName
     * @param response
     * @return
     */
    @Override
    public Result download(String fileName, HttpServletResponse response) throws IOException {
        String filePath = FILE_PARENT_PATH + "\\" + fileName;

        if(!FileUtil.exist(filePath)) return Result.build(null, ResultCodeEnum.FILE_ERROR); // 如果文件不存在

        //文件存在
        byte[] bytes = FileUtil.readBytes(filePath); // 数组是一个字节数组，也就是文件的字节流
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();

        return Result.ok(null);
    }

    /**
     * 加载文件列表（分页模糊查询）
     * @param fileName
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public Result loadFileList(String fileName, Integer pageNum, Integer pageSize, Integer fileCategory) {

        LambdaQueryWrapper<File> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(!StringUtils.isEmpty(fileName), File::getFileName, fileName);
        if(fileCategory != null) lambdaQueryWrapper.eq(File::getFileCategory, fileCategory);

        Page<File> filePage = new Page<>(pageNum, pageSize); // 在一般情况下，pageNum 值为 0 或 1 都表示请求第一页的数据
        fileMapper.selectPage(filePage, lambdaQueryWrapper);

        Map data = new HashMap();
        data.put("filePage",filePage);

        return Result.ok(data);
    }

    @Override
    public Result showAll() {
        List<File> fileList = fileMapper.selectList(null);
        Map data = new HashMap<>();
        data.put("fileList",fileList);

        return Result.ok(data);
    }
}




