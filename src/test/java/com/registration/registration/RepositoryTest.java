package com.registration.registration;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.registration.registration.objects.Church;
import com.registration.registration.objects.Leader;
import com.registration.registration.objects.Participant;
import com.registration.registration.objects.Sport;
import com.registration.registration.repositories.ChurchRepository;
import com.registration.registration.repositories.LeaderRepository;
import com.registration.registration.repositories.ParticipantRepository;
import com.registration.registration.repositories.SportRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RepositoryTest {
    
    @Autowired
    private ChurchRepository churchRepo;

    @Autowired
    private LeaderRepository leaderRepo;

    @Autowired
    private ParticipantRepository participantRepo;

    @Autowired
    private SportRepository sportRepo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void test(){
        testCreateSport("Basketball", "Basketball game", 5);
        testCreateSport("Volleyball", "Volleyball game", 6);
        testCreateSport("Badminton", "Badminton game", 1);
        testCreateSport("Chess", "Board game", 1);
        testCreateSport("Scrabble", "Board game", 1);
        testCreateChurch();
        testCreateParticipant();
        //testCreateLeader();
    }
    public void testCreateSport(String name, String description, int players){
        Sport sport = new Sport();
        sport.setName(name);
        sport.setDescription(description);
        sport.setPlayers(players);

        Sport savedSport = sportRepo.save(sport);

        Sport existSport = entityManager.find(Sport.class, savedSport.getId());

        assertThat(existSport.getName()).isEqualTo(sport.getName());
    }
    public void testCreateChurch(){
        Church church = new Church();
        church.setName("Living Hope Foursquare Gospel Church");
        church.setAddress("Prk. Papaya, Mankilam, Tagum City");

        Church savedChurch = churchRepo.save(church);

        Church existChurch = entityManager.find(Church.class, savedChurch.getId());

        assertThat(existChurch.getName()).isEqualTo(church.getName());
    }
    
    public void testCreateParticipant(){
        Participant participant = new Participant();
        participant.setEmail("Hyacinth@gmail.com");
        participant.setPassword("klhicH100");
        participant.setFirstName("Hyacinth");
        participant.setLastName("Billones");
        participant.setSex("Female");
        participant.setBirthday(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        participant.setPaid(false);
        participant.setPlayer(false);

        List<Sport> sports = new ArrayList<Sport>();
        Sport sport1 = entityManager.find(Sport.class, 1L);
        Sport sport2 = entityManager.find(Sport.class, 2L);
        System.out.println("FIND ME: " + sport1.toString() + " AND " + sport2.toString());
        sports.add(sport1);
        sports.add(sport2);
        
        participant.setSports(sports);
        
        Church church = entityManager.find(Church.class, 1L);
        if(church != null){
            participant.setChurch(church);

            Participant savedParticipant = participantRepo.save(participant);

            Participant existParticipant = entityManager.find(Participant.class, savedParticipant.getId());

            assertThat(existParticipant.getEmail()).isEqualTo(participant.getEmail());
        }
    }

    @Test
    public void testCreateLeader(){
        Leader leader = new Leader();
        leader.setEmail("Schweizer23.ss@gmail.com");
        leader.setPassword("klhicH100");
        leader.setFirstName("Ken Lloyd");
        leader.setLastName("Billones");
        leader.setSex("Male");
        leader.setAdmin(true);

        //leader.setAge(21);

        Church church = entityManager.find(Church.class, 1L);
        if(church != null){
            leader.setChurch(church);

            Leader savedLeader = leaderRepo.save(leader);

            Leader existLeader = entityManager.find(Leader.class, savedLeader.getId());

            assertThat(existLeader.getEmail()).isEqualTo(leader.getEmail());
        }
    }

    @Test
    public void testFindParticipantByEmail(){
        String email = "Schweizer23.ss@gmail.com";

        Participant participant = participantRepo.findByEmail(email);
        
        assertThat(participant).isNotNull();
    }

}
