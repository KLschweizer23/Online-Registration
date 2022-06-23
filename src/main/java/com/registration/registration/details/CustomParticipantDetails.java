package com.registration.registration.details;

import com.registration.registration.objects.Participant;

public class CustomParticipantDetails extends AbstractDetails{

    private Participant participant;

    public CustomParticipantDetails(Participant participant){
        this.participant = participant;
    }

    @Override
    public String getPassword() {
        return participant.getPassword();
    }

    @Override
    public String getUsername() {
        return participant.getEmail();
    }

}
