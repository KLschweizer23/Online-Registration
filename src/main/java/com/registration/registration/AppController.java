package com.registration.registration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import com.registration.registration.objects.Team;
import com.registration.registration.objects.abstractObjects.AbstractPerson;
import com.registration.registration.repositories.ChurchRepository;
import com.registration.registration.repositories.LeaderRepository;
import com.registration.registration.repositories.ParticipantRepository;
import com.registration.registration.repositories.SportRepository;
import com.registration.registration.repositories.TeamRepository;

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

    @Autowired
    private TeamRepository teamRepository;

    @GetMapping("")
    public ModelAndView homePage(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index.html");

        return modelAndView;
    }

    @GetMapping("/details")
    public ModelAndView detailsPage(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("details.html");

        return modelAndView;
    }

    @GetMapping("/events")
    public ModelAndView eventsPage(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("events.html");

        return modelAndView;
    }

    @GetMapping("/about")
    public ModelAndView aboutPage(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("about.html");

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
                System.out.println(leaderDetails.isAdmin());
                model.addAttribute("pendingCampers", getCampers(false, church));
            }
        }
        List<Church> listChurch = churchRepository.findAll();
        model.addAttribute("sports", getSports());
        model.addAttribute("manageSports", getSports());
        model.addAttribute("approvedCampers", getCampers(true));
        model.addAttribute("approvedLeaders", getLeaders(true));
        model.addAttribute("pendingLeaders", getLeaders(false));
        model.addAttribute("churches", listChurch);

        Team carmelTeam = teamRepository.findByName("Mt. Carmel");
        Team olivesTeam = teamRepository.findByName("Mt. Olives");
        Team sinaiTeam = teamRepository.findByName("Mt. Sinai");
        Team zionTeam = teamRepository.findByName("Mt. Zion");

        model.addAttribute("carmelTeam", getCampers(carmelTeam));
        model.addAttribute("olivesTeam", getCampers(olivesTeam));
        model.addAttribute("sinaiTeam", getCampers(sinaiTeam));
        model.addAttribute("zionTeam", getCampers(zionTeam));

        model.addAttribute("counter", new Counter());
        model.addAttribute("camper", new Participant());

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
    
    @GetMapping("/dashboard-team-reset")
    public RedirectView resetTeam(){
        //return all participants with no team
        RedirectView rv = new RedirectView();
        rv.setContextRelative(true);
        rv.setUrl("/dashboard");

        List<Participant> participants = participantRepository.findAll();
        Team noTeam = teamRepository.findById(6L).get();
        for(AbstractPerson person : participants){
            person.setTeam(noTeam);
        }
        participantRepository.saveAll(participants);
        return rv;
    }

    @GetMapping("/dashboard-team-generate")
    public RedirectView generateTeam(){
        //return all participants with no team
        RedirectView rv = new RedirectView();
        Random random = new Random();

        rv.setContextRelative(true);
        rv.setUrl("/dashboard");

        List<Participant> femaleParticipants = participantRepository.findAllByApprovedTrueAndPlayerFalseAndSexIs("female");
        List<Participant> maleParticipants = participantRepository.findAllByApprovedTrueAndPlayerFalseAndSexIs("male");

        List<Participant> carmelTeam = new ArrayList<>();
        List<Participant> oliveTeam = new ArrayList<>();
        List<Participant> sinaiTeam = new ArrayList<>();
        List<Participant> zionTeam = new ArrayList<>();

        int femaleCount = femaleParticipants.size();
        int maleCount = maleParticipants.size();
        int team = 1;

        for(int i = 0; i < femaleCount; i++, team++){
            if(team > 4)
                team = 1;
            System.out.println(team);
            int index = random.nextInt(femaleParticipants.size());
            Participant member = femaleParticipants.get(index);
            femaleParticipants.remove(index);
            switch(team){
                case 1:
                    carmelTeam.add(member);
                    break;
                case 2:
                    oliveTeam.add(member);
                    break;
                case 3:
                    sinaiTeam.add(member);
                    break;
                case 4:
                    zionTeam.add(member);
                    break;
                default:
                    System.out.println("ERROR: Too much team index");
                    break;
            }
        }
        for(int i = 0; i < maleCount; i++, team++){
            if(team > 4)
                team = 1;
            int index = random.nextInt(maleParticipants.size());
            Participant member = maleParticipants.get(index);
            maleParticipants.remove(index);
            switch(team){
                case 1:
                    carmelTeam.add(member);
                    break;
                case 2:
                    oliveTeam.add(member);
                    break;
                case 3:
                    sinaiTeam.add(member);
                    break;
                case 4:
                    zionTeam.add(member);
                    break;
                default:
                    System.out.println("ERROR: Too much team index");
                    break;
            }
        }

        assignAllMembers(carmelTeam, 1);
        assignAllMembers(oliveTeam, 2);
        assignAllMembers(sinaiTeam, 3);
        assignAllMembers(zionTeam, 4);

        return rv;
    }

    void assignAllMembers(List<Participant> team, int teamIndex){
        Team noTeam = teamRepository.findByName("None");
        Team carmelTeam = teamRepository.findByName("Mt. Carmel");
        Team oliveTeam = teamRepository.findByName("Mt. Olives");
        Team sinaiTeam = teamRepository.findByName("Mt. Sinai");
        Team zionTeam = teamRepository.findByName("Mt. Zion");

        Team[] teams = {noTeam, carmelTeam, oliveTeam, sinaiTeam, zionTeam};

        for(AbstractPerson person : team){
            person.setTeam(teams[teamIndex]);
        }

        participantRepository.saveAll(team);
    }

    @PostMapping("/dashboard_process_register")
    public RedirectView processRegisterOnSite(Participant participant, @RequestParam(value="spo", required = false) long[] spo){
        RedirectView rv = new RedirectView();
        rv.setContextRelative(true);
        rv.setUrl("/dashboard");
        participant.setPassword(participant.getLastName());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(participant.getPassword());
        participant.setPassword(encodedPassword);
        participant.setEmail(participant.getFirstName() + "@gmail.com");
        participant.setApproved(true);
        participant.setPaid(true);
        participant.setBirthday("2000-01-01");

        Team belongedTeam = new Team();

        Team carmelTeam = teamRepository.findByName("Mt. Carmel");
        Team oliveTeam = teamRepository.findByName("Mt. Olives");
        Team sinaiTeam = teamRepository.findByName("Mt. Sinai");
        Team zionTeam = teamRepository.findByName("Mt. Zion");

        long carmelCount = getCount(carmelTeam);
        long oliveCount = getCount(oliveTeam);
        long sinaiCount = getCount(sinaiTeam);
        long zionCount = getCount(zionTeam);
        
        if(carmelCount != oliveCount){
            belongedTeam = oliveTeam;
        }else{
            if(oliveCount != sinaiCount){
                belongedTeam = sinaiTeam;
            }else{
                if(sinaiCount != zionCount){
                    belongedTeam = zionTeam;
                }else{
                    belongedTeam = carmelTeam;
                }
            }
        }
        participant.setTeam(belongedTeam);
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
        return rv;
    }

    int getAvailableTeam(){

        return 0;
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
            List<Sport> sports = sportRepository.findAll();
            return sports;
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
        List<Participant> participants = approved ? participantRepository.findAllByApprovedTrueAndChurchIs(church) : participantRepository.findAllByApprovedFalseAndChurchIs(church);
        return participants;
    }

    //getLeaders either approved or not
    private List<Leader> getLeaders(boolean approved){
        List<Leader> leaders = leaderRepository.findAllByApprovedIs(approved);
        return leaders;
    }

    //get Approved Players
    private List<Participant> getCampers(Team team){
        List<Participant> players = participantRepository.findAllByApprovedTrueAndPlayerFalseAndTeamIs(team);
        return players;
    }

    //get count by team
    private long getCount(Team team){
        long count = participantRepository.countByApprovedTrueAndPlayerFalseAndTeamIs(team);
        return count;
    }
}
