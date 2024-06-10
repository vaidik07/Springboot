package com.example;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
public class TestController {

    @RequestMapping("/")
    public String home() {
        System.out.println("This is home page");
        return "Home";
    }
       
}
