package com.registration.registration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.registration.registration.details.AbstractDetails;
import com.registration.registration.objects.Church;
import com.registration.registration.objects.Counter;
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
            List<Sport> sports = new ArrayList<>();
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
    
    @GetMapping("/dashboard")
    public ModelAndView camperDashboard(Model model){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("dashboard.html");

        model.addAttribute("sports", getSports());
        model.addAttribute("campers_list", getCampers());
        model.addAttribute("counter", new Counter());

        return modelAndView;
    }
    
    @GetMapping("/login")
    public ModelAndView loginPage(Model model, @RequestParam(value = "vals", required = false) String val){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login.html");

        model.addAttribute("vals", val);

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
    public RedirectView toLoginPage(Model model, @RequestParam(value = "vals", required = false) String val){
        RedirectView rv = new RedirectView();
        rv.setContextRelative(true);
        rv.setUrl("/login");
        System.out.println(val);
        if(val != null){
            if(val.equals("leader")){
                rv.setUrl("/login?vals=leader");
            }
            else if(val.equals("camper")){
                rv.setUrl("/login?vals=camper");
            }
            else if(val.equals("error")){
                rv.setUrl("login?vals=error");
            }
        }
        
        model.addAttribute("vals", val);
        return rv;
    }
    

    //functions
    private List<Sport> getSports(){
        AbstractDetails person = (AbstractDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(person.getRole().equals("camper")){
            Participant participant = participantRepository.findByEmail(person.getEmail());
            return participant.getSports();
        }else{
            return null;
        }
    }

    private List<Participant> getCampers(){
        List<Participant> participants = participantRepository.findByApprovedTrue();
        return participants;
    }
}
