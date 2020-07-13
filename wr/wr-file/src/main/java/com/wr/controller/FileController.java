package com.wr.controller;

import com.wr.service.FileService;
import com.wr.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
//@CrossOrigin
public class FileController {
    @Autowired
    private FileService fileService;
    /**
     * 实现文件上传
     */
    @ResponseBody
    @RequestMapping("/pic/upload/{type}")
    public JsonResult fileUpLoad(MultipartFile file, @PathVariable String type){

        return JsonResult.ok(fileService.fileUpload(file,type));
    }
}
