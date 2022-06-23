package com.registration.registration.details;

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
}
