package com.registration.registration.details;

import com.registration.registration.objects.Church;
import com.registration.registration.objects.Leader;

public class CustomLeaderDetails extends AbstractDetails{
    
    private Leader leader;

    public CustomLeaderDetails(Leader leader){
        this.leader = leader;
    }

    @Override
    public String getPassword() {
        return leader.getPassword();
    }

    @Override
    public String getUsername() {
        return leader.getEmail();
    }

	@Override
	public String getFullName() {
		return leader.getFirstName() + " " + leader.getLastName();
	}

	@Override
	public String getRole() {
		return "leader";
	}

    @Override
    public Church getChurch(){
        return leader.getChurch();
    }

	@Override
	public String getFirstName() {
		return leader.getFirstName();
	}

	@Override
	public String getLastName() {
		return leader.getLastName();
	}

    public Leader getLeader(){
        return leader;
    }

    @Override
    public String getEmail() {
        return leader.getEmail();
    }

    @Override
    public String getSex() {
        return leader.getSex();
    }

    @Override
    public boolean isApproved() {
        return leader.isApproved();
    }

    public boolean isAdmin(){
        return leader.isAdmin();
    }

}
