package com.registration.registration.objects;

import com.registration.registration.objects.abstractObjects.AbstractPerson;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.lang.NonNull;

@Entity
@Table(name = "participants")
public class Participant extends AbstractPerson{

    @Column(nullable = false, length = 255)
    private String birthday;

    @NonNull
    private boolean player;

    @NonNull
    private boolean paid;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Sport> sports;

    public String getBirthday() {
        return this.birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public boolean isPlayer() {
        return this.player;
    }

    public void setPlayer(boolean player) {
        this.player = player;
    }

    public boolean isPaid() {
        return this.paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public Set<Sport> getSports() {
        return this.sports;
    }

    public void setSports(Set<Sport> sports) {
        this.sports = sports;
    }

}
