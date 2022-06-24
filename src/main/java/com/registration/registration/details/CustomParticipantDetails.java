package com.registration.registration.details;

import com.registration.registration.objects.Church;
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

	@Override
	public String getFullName() {
		return participant.getFirstName() + " " + participant.getLastName();
	}

	@Override
	public String getRole() {
		return "camper";
	}

    @Override
    public Church getChurch(){
        return participant.getChurch();
    }

	@Override
	public String getFirstName() {
		return participant.getFirstName();
	}

	@Override
	public String getLastName() {
		return participant.getLastName();
	}

    public Participant getParticipant(){
        return participant;
    }

    @Override
    public String getEmail() {
        return participant.getEmail();
    }

    @Override
    public String getSex(){
        return participant.getSex();
    }

    @Override
    public boolean isApproved() {
        return participant.isApproved();
    }

    public String getBirthday(){
        return participant.getBirthday();
    }
}
