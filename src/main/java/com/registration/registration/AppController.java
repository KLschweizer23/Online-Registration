package com.registration.registration;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class AppController {
    
    @GetMapping("")
    public ModelAndView homePage(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index.html");
        return modelAndView;
    }

    @GetMapping("/role")
    public ModelAndView rolePage(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("role.html");

        return modelAndView;
    }

}
