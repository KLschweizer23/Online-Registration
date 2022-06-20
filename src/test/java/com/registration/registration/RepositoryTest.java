package com.registration.registration;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.registration.registration.objects.Church;
import com.registration.registration.objects.Participant;
import com.registration.registration.repositories.ChurchRepository;
import com.registration.registration.repositories.LeaderRepository;
import com.registration.registration.repositories.ParticipantRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(true)
public class RepositoryTest {
    
    @Autowired
    private ChurchRepository churchRepo;

    @Autowired
    private LeaderRepository leaderRepo;

    @Autowired
    private ParticipantRepository participantRepo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateChurch(){
        Church church = new Church();
        church.setName("Living Hope Foursquare Gospel Church");
        church.setAddress("Prk. Papaya, Mankilam, Tagum City");

        Church savedChurch = churchRepo.save(church);

        Church existChurch = entityManager.find(Church.class, savedChurch.getId());

        assertThat(existChurch.getName()).isEqualTo(church.getName());
    }

    @Test
    public void testCreateParticipant(){
        Participant participant = new Participant();
        participant.setEmail("Hyacinth@gmail.com");
        participant.setPassword("klhicH100");
        participant.setFirstName("Hyacinth");
        participant.setLastName("Billones");
        participant.setBirthday(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        participant.setPaid(false);
        participant.setPlayer(false);
    }

}
