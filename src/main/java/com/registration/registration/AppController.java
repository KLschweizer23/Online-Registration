package com.registration.registration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.registration.registration.objects.Church;
import com.registration.registration.objects.Participant;
import com.registration.registration.repositories.ChurchRepository;

@RestController
public class AppController {
    
    @Autowired
    private ChurchRepository churchRepository;

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

    @GetMapping("/register-camper")
    public ModelAndView registerCamperPage(Model model){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register_camper.html");

        List<Church> listChurch = churchRepository.findAll();

        model.addAttribute("participant", new Participant());
        model.addAttribute("churches", listChurch);

        return modelAndView;
    }

}
