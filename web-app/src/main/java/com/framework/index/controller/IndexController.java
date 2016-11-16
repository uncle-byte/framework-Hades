package com.framework.index.controller;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * @author chenhaipeng
 * @version 1.0
 * @date 2016/07/18 10:30
 */

@Controller
public class IndexController {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @RequestMapping("/index")
    public String index(){
        logger.info("Fuck the world~~~~~~~~~~~~~~~~~~~");
        return "index/index";
    }

    @RequestMapping("/fileUpload")
    @ResponseBody
    public String fileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        // 判断文件是否为空
        if (!file.isEmpty()) {
            try {
                // 文件保存路径
                String filePath = request.getSession().getServletContext().getRealPath("/") + "upload/"
                        + file.getOriginalFilename();
                // 转存文件
                File localFile  = new File(filePath);
                if(!localFile.exists()){
                    FileUtils.touch(localFile);
                }
                file.transferTo(localFile);
                System.out.println(filePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 重定向
        return "success";
    }


}
