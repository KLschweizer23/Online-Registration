package com.registration.registration.objects;

import com.registration.registration.objects.abstractObjects.AbstractPerson;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.lang.NonNull;


@Entity
@Table(name = "leaders")
public class Leader extends AbstractPerson{
    
    @NonNull
    private boolean admin;

	public boolean isAdmin() {
		return this.admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

}
