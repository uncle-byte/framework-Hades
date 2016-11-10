package com.framework.index.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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


}
