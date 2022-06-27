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
import com.registration.registration.details.CustomLeaderDetails;
import com.registration.registration.objects.Church;
import com.registration.registration.objects.Counter;
import com.registration.registration.objects.Leader;
import com.registration.registration.objects.Participant;
import com.registration.registration.objects.Sport;
import com.registration.registration.repositories.ChurchRepository;
import com.registration.registration.repositories.LeaderRepository;
import com.registration.registration.repositories.ParticipantRepository;
import com.registration.registration.repositories.SportRepository;

@RestController
public class AppController {
    
    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private LeaderRepository leaderRepository;
    
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

    @GetMapping("/register-leader")
    public ModelAndView leaderCamperPage(Model model){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register_leader.html");

        List<Church> listChurch = churchRepository.findAll();

        model.addAttribute("leader", new Leader());
        model.addAttribute("churches", listChurch);
        
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

    @PostMapping("/process_leader")
    public ModelAndView processLeaderRegistration(Leader leader){

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register_success_leader.html");

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(leader.getPassword());
        leader.setPassword(encodedPassword);

        leaderRepository.save(leader);
        return modelAndView;
    }
    
    @GetMapping("/dashboard")
    public ModelAndView camperDashboard(Model model){
        ModelAndView modelAndView = new ModelAndView("thymeleaf/index");
        modelAndView.setViewName("dashboard.html");
        AbstractDetails details = (AbstractDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(details.getRole().equals("leader")){
            CustomLeaderDetails leaderDetails = (CustomLeaderDetails)details;
            Church church = details.getChurch();
            if(leaderDetails.isAdmin()){
                model.addAttribute("pendingCampers", getCampers(false));
            }else{
                model.addAttribute(("pendingCampers"), getCampers(false, church));
            }
        }
        model.addAttribute("sports", getSports());
        model.addAttribute("approvedCampers", getCampers(true));
        model.addAttribute("approvedLeaders", getLeaders(true));
        model.addAttribute("pendingLeaders", getLeaders(false));
        model.addAttribute("counter", new Counter());

        return modelAndView;
    }

    @GetMapping("/dashboard-search")
    public List<Participant> searchResult(@RequestParam(value = "keyword") String keyword){
        return getCampers(true, keyword);
    }

    @GetMapping("/dashboard-approve")
    public RedirectView approveEmail(@RequestParam(value="email")String email){
        RedirectView rv = new RedirectView();
        rv.setContextRelative(true);
        rv.setUrl("/dashboard");
        
        Participant participant = participantRepository.findByEmail(email);
        participant.setApproved(true);
        participant.setPaid(true);
        participantRepository.save(participant);
        return rv;
    }

    @GetMapping("/dashboard-reject")
    public RedirectView rejectEmail(@RequestParam(value="email")String email){
        RedirectView rv = new RedirectView();
        rv.setContextRelative(true);
        rv.setUrl("/dashboard");
        
        Participant participant = participantRepository.findByEmail(email);
        participant.setApproved(true);
        participantRepository.delete(participant);
        return rv;
    }
    
    @GetMapping("/dashboard-approve-leader")
    public RedirectView approveLeader(@RequestParam(value="email") String email){
        RedirectView rv = new RedirectView();
        rv.setContextRelative(true);
        rv.setUrl("/dashboard");

        Leader leader = leaderRepository.findByEmail(email);
        leader.setApproved(true);
        leaderRepository.save(leader);
        return rv;
    }
    
    @GetMapping("/dashboard-reject-leader")
    public RedirectView rejectLeader(@RequestParam(value="email") String email){
        RedirectView rv = new RedirectView();
        rv.setContextRelative(true);
        rv.setUrl("/dashboard");

        Leader leader = leaderRepository.findByEmail(email);
        leaderRepository.delete(leader);
        return rv;
    }
    
    @GetMapping("/dashboard-make-admin")
    public RedirectView makeAdmin(@RequestParam(value="email") String email){
        RedirectView rv = new RedirectView();
        rv.setContextRelative(true);
        rv.setUrl("/dashboard");

        Leader leader = leaderRepository.findByEmail(email);
        leader.setAdmin(true);
        leaderRepository.save(leader);
        return rv;
    }
    
    @GetMapping("/dashboard-remove-admin")
    public RedirectView removeAdmin(@RequestParam(value="email") String email){
        RedirectView rv = new RedirectView();
        rv.setContextRelative(true);
        rv.setUrl("/dashboard");

        Leader leader = leaderRepository.findByEmail(email);
        leader.setAdmin(false);
        leaderRepository.save(leader);
        return rv;
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

    //NEED REFACTOR AND MOVE TO DIFFERENT CLASS
    
    //getCampers either approved or not
    private List<Participant> getCampers(boolean approved){
        List<Participant> participants = participantRepository.findAllByApprovedIs(approved);
        return participants;
    }

    //getCampers either approved or not and contains a certain keyword
    private List<Participant> getCampers(boolean approved, String keyword){
        List<Participant> participants = approved ? participantRepository.findAllByApprovedTrueAndFirstNameContaining(keyword) : participantRepository.findAllByApprovedFalseAndFirstNameContaining(keyword);
        return participants;
    }

    //getCampers either approved or not and is equal to a certain church
    private List<Participant> getCampers(boolean approved, Church church){
        List<Participant> participants = approved ? participantRepository.findAllByApprovedFalseAndChurchIs(church) : participantRepository.findAllByApprovedTrueAndChurchIs(church);
        return participants;
    }

    //getLeaders either approved or not
    private List<Leader> getLeaders(boolean approved){
        List<Leader> leaders = leaderRepository.findAllByApprovedIs(approved);
        return leaders;
    }
}
