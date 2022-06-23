package com.registration.registration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.registration.registration.objects.Church;
import com.registration.registration.objects.Leader;
import com.registration.registration.objects.Participant;
import com.registration.registration.objects.Sport;
import com.registration.registration.repositories.ChurchRepository;
import com.registration.registration.repositories.ParticipantRepository;
import com.registration.registration.repositories.SportRepository;

@RestController
public class AppController {
    
    @Autowired
    private ParticipantRepository participantRepository;
    
    @Autowired
    private ChurchRepository churchRepository;

    @Autowired
    private SportRepository sportRepository;

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
        List<Sport> listSport = sportRepository.findAll();

        model.addAttribute("participant", new Participant());
        model.addAttribute("churches", listChurch);
        model.addAttribute("sports", listSport);
        
        return modelAndView;
    }

    @PostMapping("/process_camper")
    public ModelAndView processCamperRegistration(Participant participant, @RequestParam(value = "spo", required = false) long[] spo){

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register_success.html");

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(participant.getPassword());
        participant.setPassword(encodedPassword);

        if(spo != null){
            Sport sport = null;
            Set<Sport> sports = new HashSet<>();
            for(int i = 0; i < spo.length; i++){
                if(sportRepository.existsById((spo[i]))){
                    sport = sportRepository.findById(spo[i]).get();
                    sports.add(sport);
                }
            }
            participant.setPlayer(true);
            participant.setSports(sports);
        }

        participantRepository.save(participant);
        return modelAndView;
    }

    @GetMapping("/camper")
    public ModelAndView camperDashboard(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("camper.html");

        return modelAndView;
    }

    @GetMapping("/login")
    public ModelAndView loginPage(Model model, @RequestParam(value = "val", required = false) String val){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login.html");

        model.addAttribute("val", val);

        if(val != null){
            if(val.equals("leader")){
                model.addAttribute("user", new Leader());
            } else if(val.equals("camper")){
                model.addAttribute("user", new Participant());
            }
        }

        return modelAndView;
    }

    @PostMapping("/login-process")
    public RedirectView toLoginPage(Model model, @RequestParam(value = "val", required = false) String val){
        RedirectView rv = new RedirectView();
        rv.setContextRelative(true);
        rv.setUrl("/login");
        rv.addStaticAttribute("val", val);
        return rv;
    }

}
