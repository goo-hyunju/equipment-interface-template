package com.eqca.controller.web;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

/**
 * 일반적인 web 요청을 처리하는 Controller
 * <pre>
 * - 웹 페이지와 관련된 요청을 처리하는 컨트롤러로 클라이언트에게 HTML을 반환하는 등의 작업을 수행함
 * </pre>
 */
@Slf4j
@Controller
public class WebController implements ErrorController {

    @GetMapping("/")
    public String Index() {
        return "index.html";
    }


    @GetMapping("/redoc")
    public String redoc() {
        return "/redoc.html";
    }

}
